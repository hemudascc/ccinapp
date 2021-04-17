package net.mycomp.common.inapp.one97;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPAInAppOne97Config;
import net.jpa.repository.JPAInappTmtConfig;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.IInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.inapp.tmt.InAppTmtConfig;
import net.mycomp.common.inapp.tmt.InappTmtConstant;
import net.mycomp.common.service.IDaoService;
import net.mycomp.common.service.RedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("inappOne97Service")
public class InappOne97Service extends  AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(InappOne97Service.class.getName());
	 
	private static final String INAPP_ONE97_OTP_PREFIX="INAPP_ONE97_OTP_PREFIX";
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	
	@Autowired
	private JMSService  jmsService;
	
	@Autowired
	private  InappOne97ServiceApi inappOne97ServiceApi;
	
	@Autowired
	private JPAInAppOne97Config jpaInAppOne97Config;
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){		
		httpURLConnectionUtil=new HttpURLConnectionUtil();		 
		List<InAppOne97Config> listInAppConfig=jpaInAppOne97Config.findEnableOne97Config(true);		
		logger.info("listInAppConfig:: "+listInAppConfig);
		InappOne97Constant.mapServiiceIdToOne97InAppConfig.putAll(
				listInAppConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);	
		
	}
	
public boolean sendPin(InappProcessRequest inappProcessRequest
		,ModelAndView modelAndView){
	
	    InappOne97OtpSend one97InappOtpSend=null;
		try{
			
			one97InappOtpSend=new InappOne97OtpSend(true);
			one97InappOtpSend.setMsisdn(inappProcessRequest.getMsisdn());
			one97InappOtpSend.setCmpId(inappProcessRequest.getCmpid());
			one97InappOtpSend.setRequestId(inappProcessRequest.getId());
			one97InappOtpSend.setCgToken(inappProcessRequest.getCgToken());
			InAppOne97Config one97InAppConfig=InappOne97Constant
					.mapServiiceIdToOne97InAppConfig.get(inappProcessRequest
					.getServiceId());
			logger.info("one97InAppConfig:: "+one97InAppConfig);
		String url=InappOne97Constant
				.getUrl(one97InAppConfig.getPinSendUrl(),""+one97InappOtpSend.getCgToken()
				,inappProcessRequest.getMsisdn()
				,null,inappProcessRequest.getRequestMap().get("pubid")
				,inappProcessRequest.getRequestMap().get("ip"),null);		
		
		one97InappOtpSend.setSendOtpUrl(url);
		
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		one97InappOtpSend.setSendOtpResp(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
		//inappOtpSend.setResponseToCaller(httpResponse.getResponseStr());
		
		inappProcessRequest.setAdvertiserApiRequest(url);
		inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
		inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
		Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		if(map!=null){
		one97InappOtpSend.setTrxId(Objects.toString(map.get("txId")));
		if((httpResponse.getResponseCode()==200
				&&Objects.toString(map.get("sendOtpStatus")).equalsIgnoreCase("1"))){//pin_sent
			
			redisCacheService.putObjectCacheValueByEvictionMinute
			(InappOne97Constant.ONE97_OTP_PREFIX+one97InappOtpSend.getMsisdn(),one97InappOtpSend.getTrxId(), 30);
			inappProcessRequest.setSuccess(true);
			one97InappOtpSend.setSendOtp(true);
			return true;
		  }		
		}
		
		if((httpResponse.getResponseCode()==200&&httpResponse.getResponseStr().contains("pin_sent"))){//pin_sent
			String arr[]=httpResponse.getResponseStr().split("\\|");
			redisCacheService.putObjectCacheValueByEvictionMinute
			(INAPP_ONE97_OTP_PREFIX+one97InappOtpSend.getMsisdn(),arr[1], 30);
			inappProcessRequest.setSuccess(true);
			one97InappOtpSend.setSendOtp(true);
			return true;
		}
		
		
		}catch(Exception ex){
			logger.error("sendPin ",ex);		
		}finally{			
			jmsService.saveObject(one97InappOtpSend);
		}		
		return false;
	} 
	
	
	public boolean validatePin(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
		
		InappOne97OtpValidation one97InappOtpValidation=null;
		try{
			one97InappOtpValidation=new InappOne97OtpValidation(true);
			one97InappOtpValidation.setMsisdn(inappProcessRequest.getMsisdn());
			one97InappOtpValidation.setCmpId(inappProcessRequest.getCmpid());
			one97InappOtpValidation.setRequestId(inappProcessRequest.getId());
			one97InappOtpValidation.setCgToken(inappProcessRequest.getCgToken());
			one97InappOtpValidation.setPin(inappProcessRequest.getPin());
			one97InappOtpValidation.setTrxId(inappProcessRequest.getTxid());
			
			InAppOne97Config one97InAppConfig=InappOne97Constant
					.mapServiiceIdToOne97InAppConfig.get(inappProcessRequest
					.getServiceId());
			
		String token=Objects.toString(redisCacheService
				.getObjectCacheValue(InappOne97Constant.ONE97_OTP_PREFIX+inappProcessRequest.getMsisdn()));
	
		String url=InappOne97Constant
				.getUrl(one97InAppConfig.getPinValidationUrl(),one97InappOtpValidation.getCgToken()
				,inappProcessRequest.getMsisdn()
				,inappProcessRequest.getPin(),inappProcessRequest.getRequestMap().get("pubid")
				,inappProcessRequest.getRequestMap().get("ip"),token);	
		
		one97InappOtpValidation.setPinValidationUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		one97InappOtpValidation.setPinValidationResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		inappProcessRequest.setAdvertiserApiRequest(url);
		inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
		inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200
				&&map!=null&&(map.get("mbSubApiResponseTO"))!=null
				&&Objects.toString(((Map)map.get("mbSubApiResponseTO"))
						 .get("responseStatusCode")).equalsIgnoreCase("1")){			
				one97InappOtpValidation.setPinValidate(true);
				inappProcessRequest.setSuccess(true);
				inappProcessRequest.setConversionCount(1);
				inappProcessRequest.setPinValidateAmount(one97InAppConfig.getAmount());
				return true;
	  }
		
		String arr[]=InappOne97Constant.parse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200&&arr!=null&&arr[0].equalsIgnoreCase("0")&&
				arr[1].contains("306")){
			one97InappOtpValidation.setPinValidate(true);
			inappProcessRequest.setSuccess(true);
			inappProcessRequest.setConversionCount(1);
			inappProcessRequest.setPinValidateAmount(one97InAppConfig.getAmount());
		}
			
		}catch(Exception ex){
			logger.error("sendPin ",ex);			
		}finally{
			jmsService.saveObject(one97InappOtpValidation);
		}		
		return true;
		
	} 
	
	
	public boolean statusCheck(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
	   
		InappOne97StatusCheck one97InappStatusCheck=null;
		try{
			InAppOne97Config one97InAppConfig=InappOne97Constant
					.mapServiiceIdToOne97InAppConfig.get(inappProcessRequest
					.getServiceId());
			
			if(inappOne97ServiceApi.checkChargeStatus(inappProcessRequest
					, one97InAppConfig)){
				inappProcessRequest.setSuccess(true);
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
		
		InAppOne97Config one97InAppConfig=InappOne97Constant
				.mapServiiceIdToOne97InAppConfig.get(inappProcessRequest
				.getServiceId());
		return one97InAppConfig.getPortalUrl();
	}
	
}
