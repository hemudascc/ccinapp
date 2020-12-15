package net.mycomp.common.inapp.tmt;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPAInappTmtConfig;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.IInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
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

@Service("inappTmtService")
public class InappTmtService extends  AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(InappTmtService.class.getName());
	 
	private static final String TMT_OTP_PREFIX="TMT_OTP_PREFIX";
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAInappTmtConfig  jpaInappTmtConfig;
	
	@Autowired
	private JMSService  jmsService;
	@Autowired
	private InappTmtServiceApi inappTmtServiceApi;
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){
		
		httpURLConnectionUtil=new HttpURLConnectionUtil();
		List<InAppTmtConfig> listInAppConfig=jpaInappTmtConfig.findEnableTmtInAppConfig(true);		
		InappTmtConstant.mapServiiceIdToTmtInAppConfig.putAll(
				listInAppConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);		
//		 Integer id=daoService.
//					findNextAutoIncrementId("tb_inapp_otp_send", dbName);		
//		 InappTmtConstant.otpSendIdAtomicInteger.set(id);		   
//		 id=daoService.
//					findNextAutoIncrementId("tb_inapp_otp_validation", dbName);		
//		 InappTmtConstant.otpValidationIdAtomicInteger.set(id);		 
	}
	
public boolean sendPin(InappProcessRequest inappProcessRequest
		,ModelAndView modelAndView){
	
	    InappTmtOtpSend tmtInappOtpSend=null;
		try{
			
			tmtInappOtpSend=new InappTmtOtpSend(true);
			tmtInappOtpSend.setMsisdn(inappProcessRequest.getMsisdn());
			tmtInappOtpSend.setCmpId(inappProcessRequest.getCmpid());
			tmtInappOtpSend.setRequestId(inappProcessRequest.getId());
			tmtInappOtpSend.setCgToken(inappProcessRequest.getCgToken());
			InAppTmtConfig tmtInAppConfig=InappTmtConstant
					.mapServiiceIdToTmtInAppConfig.get(inappProcessRequest
					.getServiceId());
			if(inappTmtServiceApi.checkSubscriptionStatus(inappProcessRequest.getMsisdn()
					, inappProcessRequest.getId(), inappProcessRequest.getCgToken(), 
					tmtInAppConfig)){
				inappProcessRequest.setSuccess(false);
				return true;
			}
			 
		String url=InappTmtConstant
				.getUrl(tmtInAppConfig.getPinSendUrl(),""+tmtInAppConfig.getId()
				,inappProcessRequest.getMsisdn()
				, tmtInAppConfig, "", "");		
		
		tmtInappOtpSend.setSendOtpUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		tmtInappOtpSend.setSendOtpResp(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
		//inappOtpSend.setResponseToCaller(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){//pin_sent
			 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			 logger.info("sendPin:::::::: "+map+" ,is true:: "+Objects.toString(map.get("status")).equals("true"));
			 //{"status":true,"trxId":1025652350,"error":""}
			 tmtInappOtpSend.setTrxId(Objects.toString(map.get("trxId")));
			 if(map!=null&&Objects.toString(map.get("status")).equalsIgnoreCase("true")){
				 
				 inappProcessRequest.setSuccess(true);				 
				 tmtInappOtpSend.setSendOtp(true);
				 
			redisCacheService.putObjectCacheValueByEvictionMinute
			(TMT_OTP_PREFIX+inappProcessRequest.getMsisdn(),map.get("trxId"), 30);
			}
		 }
		
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		
		}finally{			
			jmsService.saveObject(tmtInappOtpSend);
		}		
		return true;
	} 
	
	
	public boolean validatePin(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
		
		InappTmtOtpValidation tmtInappOtpValidation=null;
		try{
			tmtInappOtpValidation=new InappTmtOtpValidation(true);
			tmtInappOtpValidation.setMsisdn(inappProcessRequest.getMsisdn());
			tmtInappOtpValidation.setCmpId(inappProcessRequest.getCmpid());
			tmtInappOtpValidation.setRequestId(inappProcessRequest.getId());
			tmtInappOtpValidation.setCgToken(inappProcessRequest.getCgToken());
			InAppTmtConfig tmtInAppConfig=InappTmtConstant
					.mapServiiceIdToTmtInAppConfig.get(inappProcessRequest
					.getServiceId());
			
		String txid=Objects.toString(redisCacheService
				.getObjectCacheValue(TMT_OTP_PREFIX+inappProcessRequest.getMsisdn()));
		String url=InappTmtConstant.getUrl(tmtInAppConfig.getPinValidationUrl(),
				""+txid ,inappProcessRequest.getMsisdn()
				, tmtInAppConfig, inappProcessRequest.getPin(), txid);
		
		tmtInappOtpValidation.setPinValidationUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		tmtInappOtpValidation.setPinValidationResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			//{"status":true,"trxId":"1025652350"}
			tmtInappOtpValidation.setTrxId(Objects.toString(map.get("trxId")));
			
			 if(map!=null&&Objects.toString(map.get("status")).equals("true")){
				 tmtInappOtpValidation.setPinValidate(true);
				 inappProcessRequest.setSuccess(true);
				 inappProcessRequest.setConversionCount(1);
				 inappProcessRequest.setPinValidateAmount(tmtInAppConfig.getAmount());
			 }
		}
		
	
		}catch(Exception ex){
			logger.error("sendPin ",ex);			
		}finally{
			jmsService.saveObject(tmtInappOtpValidation);
		}		
		return true;
		
	} 
	
	public boolean statusCheck(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
	   
		try{
			
			InAppTmtConfig tmtInAppConfig=InappTmtConstant
					.mapServiiceIdToTmtInAppConfig.get(inappProcessRequest
					.getServiceId());
			
			inappProcessRequest.setSuccess(inappTmtServiceApi.checkSubscriptionStatus(inappProcessRequest.getMsisdn()
					, inappProcessRequest.getId(), inappProcessRequest.getCgToken()
					, tmtInAppConfig));
			
		}catch(Exception ex){
			logger.error("statusCheck ",ex);
		
		}finally{
			
		}		
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		InAppTmtConfig tmtInAppConfig=InappTmtConstant
				.mapServiiceIdToTmtInAppConfig.get(inappProcessRequest
				.getServiceId());
		
		Object txid=(redisCacheService
				.getObjectCacheValue(TMT_OTP_PREFIX+inappProcessRequest.getMsisdn()));
		
		String portalUrl=null;
		if(txid!=null){			 
			 portalUrl=InappTmtConstant.getUrl(tmtInAppConfig.getPortalUrl2(),
						""+txid ,inappProcessRequest.getMsisdn()
						, tmtInAppConfig, inappProcessRequest.getPin(), ""+txid);
				
		}else{
			 portalUrl=InappTmtConstant.getUrl(tmtInAppConfig.getPortalUrl(),
						null ,inappProcessRequest.getMsisdn()
						, tmtInAppConfig, null, null);
		}		
		return portalUrl;
	}
	
	
//	public boolean statusCheck(InappProcessRequest inappProcessRequest
//			,ModelAndView modelAndView){
//	   
//		TmtInappStatusCheck tmtInappStatusCheck=null;
//		try{
//			tmtInappStatusCheck=new TmtInappStatusCheck(true);
//			tmtInappStatusCheck.setCgToken(inappProcessRequest.getCgToken());
//			tmtInappStatusCheck.setRequestId(inappProcessRequest.getId());
//			TmtInAppConfig tmtInAppConfig=TmtInappConstant
//					.mapServiiceIdToTmtInAppConfig.get(inappProcessRequest
//					.vwserviceCampaignDetail.getServiceId());
//			
//		String url=TmtInappConstant.getUrl(tmtInAppConfig.getCheckSubUrl(),
//				null ,inappProcessRequest.getMsisdn()
//				, tmtInAppConfig, "", "");
//		
//		tmtInappStatusCheck.setStatusCheckUrl(url);
//		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
//		tmtInappStatusCheck.setStatusCheckResp(httpResponse.getResponseCode()
//				+" : "+httpResponse.getResponseStr());
//		// {"status":"0","description":"active","prodid":"Have Fun Games","sc":"",
//		//"keyword":"","trxId":1025652606,"chargeStatus":false}
//		
//		if(httpResponse.getResponseCode()==200){
//			
//			Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
//			tmtInappStatusCheck.setSubStatus(Objects.toString(map.get("status")));
//			tmtInappStatusCheck.setDescription(Objects.toString(map.get("description")));
//			tmtInappStatusCheck.setTrxId(Objects.toString(map.get("trxId")));
//			tmtInappStatusCheck.setChargeStatus(Objects.toString(map.get("chargeStatus")));
//			tmtInappStatusCheck.setSubStatus(Objects.toString(map.get("status")));
//			
//			 if(map!=null&&Objects.toString(map.get("status")).equals("0")&&Objects.toString(
//					 map.get("description")).equals("active")){						
//				 inappProcessRequest.setSuccess(true);
//			}
//		 }
//		
//		}catch(Exception ex){
//			logger.error("statusCheck ",ex);
//		
//		}finally{
//			jmsService.saveObject(tmtInappStatusCheck);
//		}		
//		return true;
//	}
}
