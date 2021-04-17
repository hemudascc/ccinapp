package net.mycomp.shemaroo;

import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.service.RedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("shemarooServiceApi")
public class ShemarooServiceApi {
	
	 private static final Logger logger = Logger
				.getLogger(ShemarooServiceApi.class.getName());
	 
	
   @Autowired
   private RedisCacheService redisCacheService;
   
   @Autowired
   private JMSService jmsService;
   
	  
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	public  static void main(String arg[]){
		String json="{\"Status\": \"Success\", \"Description\": \"140499417,20191024153645700CNSNT138$NEWUSER\"}";
		 Map map= JsonMapper.getJsonToObject(json, Map.class);
		 System.out.println("sendPin:::::::: "+map
				 );
		 //{Status: “SUCCESS”, Description: “81201282,20181203114728533CNSNT138”}
		 
		 if(map!=null&&Objects.toString(map.get("Status")).equalsIgnoreCase("SUCCESS")){
			 System.out.println("sendPin:::::::: "+true
					 );
		 }
	}
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public ShemarooApiTrans sendPin(InappProcessRequest inappProcessRequest,
			ShemarooConfig shemarooConfig
			){
		
		ShemarooApiTrans shemarooApiTrans=null;
			try{
				
				shemarooApiTrans=new ShemarooApiTrans(true);
				shemarooApiTrans.setAction(MConstants.ACTION_PIN_SENT);
				shemarooApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				shemarooApiTrans.setSkMobiCampid(shemarooConfig.getCampId());
				shemarooApiTrans.setServiceId(shemarooConfig.getServiceId());				
				shemarooApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				shemarooApiTrans.setOperatoName(shemarooConfig.getOperatorName());			
			String url=ShemarooConstant
					.getUrl(shemarooConfig.getPinGenerationUrl(), null,null, inappProcessRequest.getMsisdn(),
							shemarooConfig,null,inappProcessRequest.getCgToken()
							);			
			
			shemarooApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			shemarooApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("sendPin:::::::: "+map
						 );
				 //{Status: “SUCCESS”, Description: “81201282,20181203114728533CNSNT138”}
				 
				 if(map!=null&&Objects.toString(map.get("Status")).equalsIgnoreCase("SUCCESS")){
					 
					// inappProcessRequest.setSuccess(true);				 
					 shemarooApiTrans.setSuccess(true);	
					 String description=null;
					 if(map.get("Description")!=null){
						 description=Objects.toString(map.get("Description"));
					 }
					 
				redisCacheService.putObjectCacheValueByEvictionMinute
				(ShemarooConstant.SHEMAROO_OTP_TRANS_ID_PREFIX
						+inappProcessRequest.getMsisdn(),description, 30);
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(shemarooApiTrans);
			}		
			return shemarooApiTrans;
		} 
	
	
	public ShemarooApiTrans resendPin(InappProcessRequest inappProcessRequest,
			ShemarooConfig shemarooConfig,String subscriptioncontractid
			){
		
		ShemarooApiTrans shemarooApiTrans=null;
			try{
				
				shemarooApiTrans=new ShemarooApiTrans(true);
				shemarooApiTrans.setAction(ShemarooConstant.RESEND_PIN);
				shemarooApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				shemarooApiTrans.setSkMobiCampid(shemarooConfig.getCampId());
				shemarooApiTrans.setServiceId(shemarooConfig.getServiceId());				
				shemarooApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				shemarooApiTrans.setOperatoName(shemarooConfig.getOperatorName());			
			String url=ShemarooConstant
					.getUrl(shemarooConfig.getResendPinUrl(), subscriptioncontractid,null, inappProcessRequest.getMsisdn(),
							shemarooConfig,null,inappProcessRequest.getCgToken()
							);			
			
			shemarooApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			shemarooApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("resendPin:::::::: "+map);
				 //{Status: “SUCCESS”}
				 
				 if(map!=null&&Objects.toString(map.get("Status")).equalsIgnoreCase("SUCCESS")){					 
					// inappProcessRequest.setSuccess(true);				 
					 shemarooApiTrans.setSuccess(true);						 
			 }
			}
			}catch(Exception ex){
				logger.error("resendPin ",ex);
			
			}finally{			
				jmsService.saveObject(shemarooApiTrans);
			}		
			return shemarooApiTrans;
		} 
	
	
	public ShemarooApiTrans validatePin(InappProcessRequest inappProcessRequest,ShemarooConfig shemarooConfig
			){
		
		ShemarooApiTrans shemarooApiTrans=null;
			try{
				
				shemarooApiTrans=new ShemarooApiTrans(true);
				shemarooApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
				shemarooApiTrans.setOperatoName(shemarooConfig.getOperatorName());				
				shemarooApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				shemarooApiTrans.setSkMobiCampid(shemarooConfig.getCampId());
				shemarooApiTrans.setServiceId(shemarooConfig.getServiceId());				
				shemarooApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				String description=(String)redisCacheService.getObjectCacheValue
				(ShemarooConstant.SHEMAROO_OTP_TRANS_ID_PREFIX
						+inappProcessRequest.getMsisdn());
				String str[]=description.split(",");
			
			String url=ShemarooConstant
					.getUrl(shemarooConfig.getPinVerificationUrl(), str[0],str[1], 
							inappProcessRequest.getMsisdn(),
							shemarooConfig,inappProcessRequest.getPin(),null
							);			
			shemarooApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			shemarooApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("validatePin:::::::: "+map);
				 //: {Status: “SUCCESS”}
				 
				 if(map!=null&&Objects.toString(map.get("Status")).equalsIgnoreCase("SUCCESS")){
					 
					// inappProcessRequest.setSuccess(true);				 
					 shemarooApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(shemarooApiTrans);
			}		
			return shemarooApiTrans;
		} 
	
	
	public ShemarooApiTrans isSubscribedUser(InappProcessRequest inappProcessRequest,ShemarooConfig shemarooConfig
			){
		
		ShemarooApiTrans shemarooApiTrans=null;
			try{				
				shemarooApiTrans=new ShemarooApiTrans(true);
				shemarooApiTrans.setAction(MConstants.ACTION_STATUS_CHECK);
				shemarooApiTrans.setOperatoName(shemarooConfig.getOperatorName());				
				shemarooApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				shemarooApiTrans.setSkMobiCampid(shemarooConfig.getCampId());
				shemarooApiTrans.setServiceId(shemarooConfig.getServiceId());				
				shemarooApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				
				
			
			String url=ShemarooConstant
					.getUrl(shemarooConfig.getStatusCheckUrl(),null, null, 
							inappProcessRequest.getMsisdn(),shemarooConfig,
							null,null
							);			
			shemarooApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			shemarooApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200){//pin_sent		
				
				 if(!httpResponse.getResponseStr().equalsIgnoreCase("NEWUSER")){//					 
					 //inappProcessRequest.setSuccess(true);				 
					 shemarooApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("isSubscribedUser ",ex);
			
			}finally{			
				jmsService.saveObject(shemarooApiTrans);
			}		
			return shemarooApiTrans;
		} 
}
