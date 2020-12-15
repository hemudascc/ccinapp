package net.mycomp.ascenco;

import java.util.HashMap;
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

@Service("ascencoServiceApi")
public class AscencoServiceApi {
	
	 private static final Logger logger = Logger
				.getLogger(AscencoServiceApi.class.getName());
	 
	
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
	
	public AscencoApiTrans sendPin(InappProcessRequest inappProcessRequest,
			AscencoConfig ascencoConfig
			){
		
		AscencoApiTrans ascencoApiTrans=null;
			try{
				
				ascencoApiTrans=new AscencoApiTrans(true);
				ascencoApiTrans.setAction(MConstants.ACTION_PIN_SENT);
				ascencoApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				ascencoApiTrans.setSkMobiCampid(ascencoConfig.getCampId());
				ascencoApiTrans.setServiceId(ascencoConfig.getServiceId());				
				ascencoApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				ascencoApiTrans.setOperatoName(ascencoConfig.getOperatorName());			
			String url=AscencoConstant.getUrl(ascencoConfig.getPinGenerationUrl(), 
					inappProcessRequest.getMsisdn(), ascencoConfig, null, null);
					;
           Map<String,String> headerMap=new HashMap<String,String>();			
			headerMap.put("Accept","application/json");
			headerMap.put("Content-Type","application/json");
			headerMap.put("Authorization",ascencoConfig.getAuthorization());
			
			//Map<String,String> data=new HashMap<String,String>();
			//data.put("msisdn", ascencoApiTrans.getMsisdn());
			//String json=JsonMapper.getObjectToJson(data);
			ascencoApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url, headerMap);
			ascencoApiTrans.setResponse(httpResponse.getResponseStr());
			ascencoApiTrans.setResponseCode(httpResponse.getResponseCode());
			
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("sendPin:::::::: "+map );
				//{"body": {"msisdn": "","product_id": "","txId": "",
				 //"correlatorId": "","sendOtpStatus": "","sendOtpStatusDesc": "",
				 //"flag": ""},"statusCode": OK","statusCodeValue": 200} 
				 if(map!=null&&Objects.toString(map.get("statusCode")).equalsIgnoreCase("OK")){
					String txId= Objects.toString(((Map)map.get("body")).get("txId"));
					 redisCacheService.putObjectCacheValueByEvictionMinute(
							 AscencoConstant.ASCENCO_OTP_TRANS_ID_PREFIX+ascencoApiTrans.getMsisdn()
							 , txId, 5);					 
					 ascencoApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(ascencoApiTrans);
			}		
			return ascencoApiTrans;
		} 
	
	
	
	
	
	public AscencoApiTrans validatePin(InappProcessRequest inappProcessRequest,AscencoConfig ascencoConfig
			){
		
		AscencoApiTrans ascencoApiTrans=null;
			try{
				
				ascencoApiTrans=new AscencoApiTrans(true);
				ascencoApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
				ascencoApiTrans.setOperatoName(ascencoConfig.getOperatorName());				
				ascencoApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				ascencoApiTrans.setSkMobiCampid(ascencoConfig.getCampId());
				ascencoApiTrans.setServiceId(ascencoConfig.getServiceId());				
				ascencoApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				String txId= Objects.toString(redisCacheService.getObjectCacheValue(
						 AscencoConstant.ASCENCO_OTP_TRANS_ID_PREFIX+ascencoApiTrans.getMsisdn()
						));
			
			String url=AscencoConstant.getUrl(ascencoConfig.getPinVerificationUrl(),
					ascencoApiTrans.getMsisdn(), ascencoConfig, inappProcessRequest.getPin(), txId);
			//ascencoApiTrans.setRequest(url);
			Map<String,String> headerMap=new HashMap<String,String>();
			
			headerMap.put("Accept","application/json");
			headerMap.put("Content-Type","application/json");
			headerMap.put("Authorization",ascencoConfig.getAuthorization());
			//Map<String,String> data=new HashMap<String,String>();
			//data.put("msisdn", ascencoApiTrans.getMsisdn());
			//data.put("otp", inappProcessRequest.getPin());
			//String json=JsonMapper.getObjectToJson(data);
			ascencoApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url, headerMap);
			
			ascencoApiTrans.setResponse(httpResponse.getResponseStr());
			ascencoApiTrans.setResponseCode(httpResponse.getResponseCode());
			//{"body": {"msisdn": "","product_id": "","op_id": "","requestStatusCode": "","requestStatusDesc": "","flag": ""}
			//,"statusCode": "OK","statusCodeValue": 200}
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("validatePin:::::::: "+map);
				 //: {Status: “SUCCESS”}
				 
				 if(map!=null&&Objects.toString(map.get("statusCode")).equalsIgnoreCase("OK")){
					 
					// inappProcessRequest.setSuccess(true);				 
					 ascencoApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(ascencoApiTrans);
			}		
			return ascencoApiTrans;
		} 
	
	
	public AscencoApiTrans statusCheck(InappProcessRequest inappProcessRequest,AscencoConfig ascencoConfig
			){
		
		AscencoApiTrans ascencoApiTrans=null;
			try{
				
				ascencoApiTrans=new AscencoApiTrans(true);
				ascencoApiTrans.setAction(MConstants.ACTION_STATUS_CHECK);
				ascencoApiTrans.setOperatoName(ascencoConfig.getOperatorName());				
				ascencoApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				ascencoApiTrans.setSkMobiCampid(ascencoConfig.getCampId());
				ascencoApiTrans.setServiceId(ascencoConfig.getServiceId());				
				ascencoApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				
			
			String url=AscencoConstant.getUrl(ascencoConfig.getStatusCheckUrl(),
					ascencoApiTrans.getMsisdn(), ascencoConfig, null, null);
			//ascencoApiTrans.setRequest(url);
			Map<String,String> headerMap=new HashMap<String,String>();
			
			headerMap.put("Accept","application/json");
			headerMap.put("Content-Type","application/json");
			headerMap.put("Authorization",ascencoConfig.getAuthorization());
			//Map<String,String> data=new HashMap<String,String>();
			//data.put("msisdn", ascencoApiTrans.getMsisdn());
			//data.put("otp", inappProcessRequest.getPin());
			//String json=JsonMapper.getObjectToJson(data);
			ascencoApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url, headerMap);
			
			ascencoApiTrans.setResponse(httpResponse.getResponseStr());
			ascencoApiTrans.setResponseCode(httpResponse.getResponseCode());
			//{"body": {"msisdn": "","product_id": "","op_id": "","requestStatusCode": "","requestStatusDesc": "","flag": ""}
			//,"statusCode": "OK","statusCodeValue": 200}
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("statusCheck:::::::: "+map);
				 //: {Status: “SUCCESS”}
				 
				 if(map!=null&&Objects.toString(map.get("isSubscribed")).equalsIgnoreCase("true")){
					 
					// inappProcessRequest.setSuccess(true);				 
					 ascencoApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("statusCheck ",ex);
			
			}finally{			
				jmsService.saveObject(ascencoApiTrans);
			}		
			return ascencoApiTrans;
		} 
	
	
}
