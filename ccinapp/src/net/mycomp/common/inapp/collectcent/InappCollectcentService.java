package net.mycomp.common.inapp.collectcent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPACollectcentServiceConfig;
import net.jpa.repository.JPAInAppOne97Config;
import net.jpa.repository.JPAInappTmtConfig;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.IInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.inapp.tmt.InAppTmtConfig;
import net.mycomp.common.inapp.tmt.InappTmtConstant;
import net.mycomp.common.service.IDaoService;
import net.mycomp.common.service.RedisCacheService;
import net.util.EnumReason;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.sun.xml.bind.v2.model.core.EnumConstant;

@Service("inappCollectcentService")
public class InappCollectcentService extends  AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(InappCollectcentService.class.getName());
	 
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	
	@Autowired
	private JMSService  jmsService;
	
	@Autowired
	private JPACollectcentServiceConfig jpaCollectcentServiceConfig;
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){		
		
		httpURLConnectionUtil=new HttpURLConnectionUtil();		 
		List<CollectcentServiceConfig> listInAppConfig=jpaCollectcentServiceConfig
				.findEnableCollectcentServiceConfig(true);		
		logger.info("listInAppConfig:: "+listInAppConfig);
		CollectcentConstant.mapServiiceIdToCollectcentServiceConfig.putAll(
				listInAppConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);	
		
	}
	
public boolean sendPin(InappProcessRequest inappProcessRequest
		,ModelAndView modelAndView){
	
	    InappCollectcentOtpSend inappCollectcentOtpSend=null;
		try{
			
			inappCollectcentOtpSend=new InappCollectcentOtpSend(true);
			inappCollectcentOtpSend.setMsisdn(inappProcessRequest.getMsisdn());
			inappCollectcentOtpSend.setCmpId(inappProcessRequest.getCmpid());
			inappCollectcentOtpSend.setRequestId(inappProcessRequest.getId());
			inappCollectcentOtpSend.setCgToken(inappProcessRequest.getCgToken());
			
			CollectcentServiceConfig collectcentServiceConfig=CollectcentConstant
					.mapServiiceIdToCollectcentServiceConfig.get(inappProcessRequest
					.getServiceId());
			logger.info("collectcentServiceConfig:: "+collectcentServiceConfig);
		String url=CollectcentConstant
				.getUrl(collectcentServiceConfig.getPinSendUrl(),""+inappCollectcentOtpSend.getCgToken()
				,inappProcessRequest.getMsisdn()
				,null,inappProcessRequest.getRequestMap().get("pubid")
				,inappProcessRequest.getRequestMap().get("ip"),null);		
		
		inappCollectcentOtpSend.setSendOtpUrl(url);
		
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		inappCollectcentOtpSend.setSendOtpResp(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
		inappProcessRequest.setAdvertiserApiRequest(url);
		inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
		inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
		
		//inappOtpSend.setResponseToCaller(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200
				&&Objects.toString(httpResponse.getResponseStr()).equalsIgnoreCase("1")){//pin_sent			
			redisCacheService.putObjectCacheValueByEvictionMinute
			(CollectcentConstant.COLLECTCENT_OTP_PREFIX+
					inappCollectcentOtpSend.getMsisdn(),inappCollectcentOtpSend.getTrxId(), 30);
			inappProcessRequest.setSuccess(true);
			inappCollectcentOtpSend.setSendOtp(true);
			inappProcessRequest.setSendPinCount(1);
			return true;
		  }	
		
		  Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		  inappProcessRequest.enumReason= EnumReason.findCuase(Objects.toString(map.get("errorMessage")));
		 if(map!=null&&(Objects.toString(map.get("STATUS")).equalsIgnoreCase("SUCCESS")
				 ||Objects.toString(map.get("response")).equalsIgnoreCase("SUCCESS"))){			 
			    inappProcessRequest.setSuccess(true);
				inappCollectcentOtpSend.setSendOtp(true);
				inappProcessRequest.setSendPinCount(1);
				return true;				
		  }else if(map!=null&&(Objects.toString(map.get("response")).equalsIgnoreCase("SUBSCRIBED"))){
			  inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);
		  }
		
		 
		}catch(Exception ex){
			logger.error("sendPin ",ex);		
		}finally{			
			jmsService.saveObject(inappCollectcentOtpSend);
		}		
		return false;
	} 
	
	
	public boolean validatePin(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
		
		InappCollectcentValidation inappCollectcentValidation=null;
		try{
			inappCollectcentValidation=new InappCollectcentValidation(true);
			inappCollectcentValidation.setMsisdn(inappProcessRequest.getMsisdn());
			inappCollectcentValidation.setCmpId(inappProcessRequest.getCmpid());
			inappCollectcentValidation.setRequestId(inappProcessRequest.getId());
			inappCollectcentValidation.setCgToken(inappProcessRequest.getCgToken());
			inappCollectcentValidation.setPin(inappProcessRequest.getPin());
			inappCollectcentValidation.setTrxId(inappProcessRequest.getTxid());
			
			CollectcentServiceConfig collectcentServiceConfig=CollectcentConstant
					.mapServiiceIdToCollectcentServiceConfig.get(inappProcessRequest
					.getServiceId());
			logger.info("collectcentServiceConfig:: "+collectcentServiceConfig);
			
		String token=Objects.toString(redisCacheService
				.getObjectCacheValue(CollectcentConstant.COLLECTCENT_OTP_PREFIX+inappProcessRequest.getMsisdn()));
	
		String url=CollectcentConstant
				.getUrl(collectcentServiceConfig.getPinValidationUrl(),inappCollectcentValidation.getCgToken()
				,inappProcessRequest.getMsisdn()
				,inappProcessRequest.getPin(),inappProcessRequest.getRequestMap().get("pubid")
				,inappProcessRequest.getRequestMap().get("ip"),token);	
		
		inappCollectcentValidation.setPinValidationUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		inappCollectcentValidation.setPinValidationResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		inappProcessRequest.setAdvertiserApiRequest(url);
		inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
		inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200
				&&Objects.toString(httpResponse.getResponseStr()).equalsIgnoreCase("1")){			
			    inappCollectcentValidation.setPinValidate(true);
				inappProcessRequest.setSuccess(true);
				inappProcessRequest.setConversionCount(1);
				inappProcessRequest.setPinValidateAmount(collectcentServiceConfig.getAmount());
				return true;
	  }
		
		Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		 if(map!=null&&(Objects.toString(map.get("STATUS")).equalsIgnoreCase("SUCCESS")
				 ||Objects.toString(map.get("response")).equalsIgnoreCase("SUCCESS"))){
			 
			    inappCollectcentValidation.setPinValidate(true);
				inappProcessRequest.setSuccess(true);
				inappProcessRequest.setConversionCount(1);
				inappProcessRequest.setPinValidateAmount(collectcentServiceConfig.getAmount());
				return true;
		  }else if(map!=null&&(Objects.toString(map.get("response")).equalsIgnoreCase("SUBSCRIBED"))){
			  inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);
		  }
		
			
		}catch(Exception ex){
			logger.error("sendPin ",ex);			
		}finally{
			jmsService.saveObject(inappCollectcentValidation);
		}		
		return true;
		
	} 
	
	
	public boolean statusCheck(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
	   
		InappCollectcentStatusCheck one97InappStatusCheck=null;
		try{			
			CollectcentServiceConfig collectcentServiceConfig=CollectcentConstant
					.mapServiiceIdToCollectcentServiceConfig.get(inappProcessRequest
					.getServiceId());
			logger.info("collectcentServiceConfig:: "+collectcentServiceConfig);
			String url=CollectcentConstant
					.getUrl(collectcentServiceConfig.getCheckSubUrl(),inappProcessRequest.getCgToken()
					,inappProcessRequest.getMsisdn()
					,inappProcessRequest.getPin(),inappProcessRequest.getRequestMap().get("pubid")
					,inappProcessRequest.getRequestMap().get("ip"),inappProcessRequest.getCgToken());
			HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
			
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			
			if(httpResponse.getResponseCode()==200
					&&Objects.toString(httpResponse.getResponseStr()).equalsIgnoreCase("1")){				   
					inappProcessRequest.setSuccess(true);					
					return true;
		    }
			
			if(httpResponse.getResponseCode()==200
					&&Objects.toString(httpResponse.getResponseStr()).equalsIgnoreCase("ACTIVE")){				   
					inappProcessRequest.setSuccess(true);					
					return true;
		  }
		}catch(Exception ex){
			logger.error("statusCheck ",ex);
		
		}finally{
			jmsService.saveObject(one97InappStatusCheck);
		}		
		return true;
	}
	
	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		CollectcentServiceConfig collectcentServiceConfig=CollectcentConstant
				.mapServiiceIdToCollectcentServiceConfig.get(inappProcessRequest
				.getServiceId());
		logger.info("collectcentServiceConfig:: "+collectcentServiceConfig);
		String url=CollectcentConstant
				.getUrl(collectcentServiceConfig.getPortalUrl(),inappProcessRequest.getCgToken()
				,inappProcessRequest.getMsisdn()
				,inappProcessRequest.getPin(),inappProcessRequest.getRequestMap().get("pubid")
				,inappProcessRequest.getRequestMap().get("ip"),inappProcessRequest.getCgToken());
		inappProcessRequest.setAdvertiserApiRequest(url);
		//inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
		//inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
		
		return url;
	}

	@Override
	public boolean dct(InappProcessRequest inappProcessRequest) {
		
		CollectcentServiceConfig collectcentServiceConfig=CollectcentConstant
				.mapServiiceIdToCollectcentServiceConfig.get(inappProcessRequest
				.getServiceId());
		logger.info("collectcentServiceConfig:: "+collectcentServiceConfig);
		String url=CollectcentConstant
				.getUrl(collectcentServiceConfig.getPortalUrl(),inappProcessRequest.getCgToken()
				,inappProcessRequest.getMsisdn()
				,inappProcessRequest.getPin(),inappProcessRequest.getRequestMap().get("pubid")
				,inappProcessRequest.getRequestMap().get("ip"),inappProcessRequest.getCgToken());
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		inappProcessRequest.setAdvertiserApiRequest(url);
		inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
		inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
		
		Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		
		 if(map!=null&&(Objects.toString(map.get("STATUS")).equalsIgnoreCase("SUCCESS"))){		
				inappProcessRequest.setSuccess(true);				
				return true;
		  }
		 if(map!=null&&(Objects.toString(map.get("status")).equalsIgnoreCase("true"))){		
				inappProcessRequest.setSuccess(true);				
				return true;
		  }
		 return false;
	}
	
}
