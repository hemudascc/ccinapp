package net.mycomp.kineticdigital;

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

@Service("kinaticServiceApi")
public class KinaticServiceApi {
	
	 private static final Logger logger = Logger
				.getLogger(KinaticServiceApi.class.getName());
	 
	
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
	
	public KinaticApiTrans sendPin(InappProcessRequest inappProcessRequest,
			KinaticConfig kinaticConfig
			){
		
		KinaticApiTrans kinaticApiTrans=null;
			try{
				
				kinaticApiTrans=new KinaticApiTrans(true);
				kinaticApiTrans.setAction(MConstants.ACTION_PIN_SENT);
				kinaticApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				kinaticApiTrans.setSkMobiCampid(kinaticConfig.getCampId());
				kinaticApiTrans.setServiceId(kinaticConfig.getServiceId());				
				kinaticApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				kinaticApiTrans.setOperatoName(kinaticConfig.getOperatorName());			
			String url=KinaticConstant.getUrl(kinaticConfig.getPinGenerationUrl(),
					kinaticApiTrans.getMsisdn(), kinaticConfig, null, kinaticApiTrans.getTokenToCg());
			
			
Map<String,String> headerMap=new HashMap<String,String>();
			
			headerMap.put("Accept","application/json");
			headerMap.put("Content-Type","application/json");
			
			Map<String,String> data=new HashMap<String,String>();
			data.put("msisdn", kinaticApiTrans.getMsisdn());
			String json=JsonMapper.getObjectToJson(data);
			kinaticApiTrans.setRequest(url+" : "+json);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url,
					json
					,headerMap);
			kinaticApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			
			if(httpResponse.getResponseCode()==200||httpResponse.getResponseCode()==302){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("sendPin:::::::: "+map
						 );
				 //{Status: “SUCCESS”, Description: “81201282,20181203114728533CNSNT138”}
				 
				 if(map!=null&&Objects.toString(map.get("response")).equalsIgnoreCase("SUCCESS")){					 			 
					 kinaticApiTrans.setSuccess(true);	
				  }
				 
				 if(map!=null&&Objects.toString(map.get("STATUS")).equalsIgnoreCase("SUCCESS")){					 			 
					 kinaticApiTrans.setSuccess(true);	
				  }
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(kinaticApiTrans);
			}		
			return kinaticApiTrans;
		} 
	
	
	
	
	
	public KinaticApiTrans validatePin(InappProcessRequest inappProcessRequest,KinaticConfig kinaticConfig
			){
		
	    	KinaticApiTrans kinaticApiTrans=null;
			try{
				
				kinaticApiTrans=new KinaticApiTrans(true);
				kinaticApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
				kinaticApiTrans.setOperatoName(kinaticConfig.getOperatorName());				
				kinaticApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				kinaticApiTrans.setSkMobiCampid(kinaticConfig.getCampId());
				kinaticApiTrans.setServiceId(kinaticConfig.getServiceId());				
				kinaticApiTrans.setTokenToCg(inappProcessRequest.getCgToken());			
			
			String url=KinaticConstant.getUrl(kinaticConfig.getPinVerificationUrl(), kinaticApiTrans.getMsisdn()
					, kinaticConfig, inappProcessRequest.getPin(), kinaticApiTrans.getTokenToCg());
			//kinaticApiTrans.setRequest(url);
			Map<String,String> headerMap=new HashMap<String,String>();
			
			headerMap.put("Accept","application/json");
			headerMap.put("Content-Type","application/json");
			
			Map<String,String> data=new HashMap<String,String>();
			data.put("msisdn", kinaticApiTrans.getMsisdn());
			data.put("otp", inappProcessRequest.getPin());
			String json=JsonMapper.getObjectToJson(data);
			kinaticApiTrans.setRequest(url+" : "+json);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url,
					json
					,headerMap);
			kinaticApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("validatePin:::::::: "+map);
				 //: {Status: “SUCCESS”}
				 
				 if(map!=null&&Objects.toString(map.get("response")).equalsIgnoreCase("SUCCESS")){								 
					 kinaticApiTrans.setSuccess(true);	
				}
			
				 if(map!=null&&Objects.toString(map.get("STATUS")).equalsIgnoreCase("SUCCESS")){					 			 
					 kinaticApiTrans.setSuccess(true);	
				  }
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(kinaticApiTrans);
			}		
			return kinaticApiTrans;
		} 
	
	
	public KinaticApiTrans checkStatus(InappProcessRequest inappProcessRequest,
			KinaticConfig kinaticConfig
			){
		
		KinaticApiTrans kinaticApiTrans=null;
			try{
				
				kinaticApiTrans=new KinaticApiTrans(true);
				kinaticApiTrans.setAction(MConstants.ACTION_STATUS_CHECK);
				kinaticApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				kinaticApiTrans.setSkMobiCampid(kinaticConfig.getCampId());
				kinaticApiTrans.setServiceId(kinaticConfig.getServiceId());				
				kinaticApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				kinaticApiTrans.setOperatoName(kinaticConfig.getOperatorName());			
			String url=KinaticConstant.getUrl(kinaticConfig.getStatusCheckUrl(),
					kinaticApiTrans.getMsisdn(), kinaticConfig, null, kinaticApiTrans.getTokenToCg());
			
			
            Map<String,String> headerMap=new HashMap<String,String>();
			
			headerMap.put("Accept","application/json");
			headerMap.put("Content-Type","application/json");
			
			Map<String,String> data=new HashMap<String,String>();
			data.put("msisdn", kinaticApiTrans.getMsisdn());
			String json=JsonMapper.getObjectToJson(data);
			kinaticApiTrans.setRequest(url+" : "+json);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url,
					json
					,headerMap);
			kinaticApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			
			if(httpResponse.getResponseCode()==200||httpResponse.getResponseCode()==302){//pin_sent
			
				 if(httpResponse.getResponseStr().equalsIgnoreCase("1")){					 			 
					 kinaticApiTrans.setSuccess(true);	
				  }
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(kinaticApiTrans);
			}		
			return kinaticApiTrans;
		} 
	
	
}
