package net.mycomp.inapp.adacts;

import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.jms.JMSService;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.service.RedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MConstants;

@Service("adactsServiceApi")
public class AdactsServiceApi {
	
	 private static final Logger logger = Logger
				.getLogger(AdactsServiceApi.class.getName());
	 
	
   @Autowired
   private RedisCacheService redisCacheService;
   
   @Autowired
   private JMSService jmsService;
   
	  
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public AdactsApiTrans sendPin(InappProcessRequest inappProcessRequest,AdactsConfig adactsConfig
			){
		
		AdactsApiTrans adactsApiTrans=null;
			try{
				
				adactsApiTrans=new AdactsApiTrans(true);
				adactsApiTrans.setAction(MConstants.ACTION_PIN_SENT);
				adactsApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				adactsApiTrans.setAdactsCampid(adactsConfig.getCampId());
				adactsApiTrans.setServiceId(adactsConfig.getServiceId());				
				adactsApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				adactsApiTrans.setOperatoName(adactsConfig.getOperatorName());			
			String url=AdactsConstant
					.getUrl(adactsConfig.getPinGenerationUrl(), null, inappProcessRequest.getMsisdn(),
							adactsConfig,null,inappProcessRequest.getCgToken()
							);			
			adactsApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			adactsApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("sendPin:::::::: "+map+" ,is true:: "+Objects.toString(map.get("status")).equals("true"));
				 //{"status": true | false,"message": "OTP_GENERATION_SUCCESS | OTP_GENERATION_FAILED |NUMBER_BLOCKED"}
				 
				 if(map!=null&&(Objects.toString(map.get("status")).equalsIgnoreCase("true") || 
						 Objects.toString(map.get("success")).equalsIgnoreCase("true"))){
					 
					 inappProcessRequest.setSuccess(true);				 
					 adactsApiTrans.setSuccess(true);	
					 String subRefId=Objects.toString(map.get("subid")!=null?Objects.toString(map.get("subid")):adactsApiTrans.getTokenToCg());
					 
				redisCacheService.putObjectCacheValueByEvictionMinute
				(AdactsConstant.ADACTS_OTP_TRANS_ID_PREFIX
						+inappProcessRequest.getMsisdn(),subRefId, 30);
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(adactsApiTrans);
			}		
			return adactsApiTrans;
		} 
	
	public AdactsApiTrans validatePin(InappProcessRequest inappProcessRequest,AdactsConfig adactsConfig
			){
		
		AdactsApiTrans adactsApiTrans=null;
			try{
				
				adactsApiTrans=new AdactsApiTrans(true);
				adactsApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
				adactsApiTrans.setOperatoName(adactsConfig.getOperatorName());				
				adactsApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				adactsApiTrans.setAdactsCampid(adactsConfig.getCampId());
				adactsApiTrans.setServiceId(adactsConfig.getServiceId());				
				adactsApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				String subscriptionContractId=(String)redisCacheService.getObjectCacheValue
				(AdactsConstant.ADACTS_OTP_TRANS_ID_PREFIX
						+inappProcessRequest.getMsisdn());
				
			
			String url=AdactsConstant
					.getUrl(adactsConfig.getPinVerificationUrl(), subscriptionContractId, 
							inappProcessRequest.getMsisdn(),
							adactsConfig,inappProcessRequest.getPin(),null
							);			
			adactsApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			adactsApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("sendPin:::::::: "+map);
				 //{"status": true | false,"message": "OTP_VERIFIED | OTP_VERIFICATION_FAILED","login_url": "DIRECT_LOGIN_URL"}
				 
				 if(map!=null&&(Objects.toString(map.get("status")).equalsIgnoreCase("true") || 
						 Objects.toString(map.get("success")).equalsIgnoreCase("true"))){
					 
					// inappProcessRequest.setSuccess(true);				 
					 adactsApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(adactsApiTrans);
			}		
			return adactsApiTrans;
		} 
	
	
	public AdactsApiTrans isSubscribedUser(InappProcessRequest inappProcessRequest,AdactsConfig adactsConfig
			){
		
		AdactsApiTrans adactsApiTrans=null;
			try{				
				adactsApiTrans=new AdactsApiTrans(true);
				adactsApiTrans.setAction(MConstants.ACTION_STATUS_CHECK);
				adactsApiTrans.setOperatoName(adactsConfig.getOperatorName());				
				adactsApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				adactsApiTrans.setAdactsCampid(adactsConfig.getCampId());
				adactsApiTrans.setServiceId(adactsConfig.getServiceId());				
				adactsApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				
				
			
			String url=AdactsConstant
					.getUrl(adactsConfig.getStatusCheckUrl(), null, 
							inappProcessRequest.getMsisdn(),adactsConfig,
							null,null
							);			
			adactsApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			adactsApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("isSubscribedUser:::::::: "+map);
			 //{"status": true | false,"message": "FOUND | NOT_FOUND","login_url": "DIRECT_LOGIN_URL"}

				 if(map!=null&&(Objects.toString(map.get("status")).equalsIgnoreCase("true") || 
						 Objects.toString(map.get("success")).equalsIgnoreCase("true"))){//					 
					 //inappProcessRequest.setSuccess(true);				 
					 adactsApiTrans.setSuccess(true);	
					 adactsApiTrans.setPortalURL(Objects.toString(map.get("login_url")));	
				}
			 }
			
			}catch(Exception ex){
				logger.error("isSubscribedUser ",ex);
			
			}finally{			
				jmsService.saveObject(adactsApiTrans);
			}		
			return adactsApiTrans;
		} 
	
	
}
