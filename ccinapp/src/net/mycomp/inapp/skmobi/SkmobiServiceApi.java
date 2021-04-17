package net.mycomp.inapp.skmobi;

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
import org.springframework.web.servlet.ModelAndView;

@Service("skmobiServiceApi")
public class SkmobiServiceApi {
	
	 private static final Logger logger = Logger
				.getLogger(SkmobiServiceApi.class.getName());
	 
	
   @Autowired
   private RedisCacheService redisCacheService;
   
   @Autowired
   private JMSService jmsService;
   
	  
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public SkmobiApiTrans sendPin(InappProcessRequest inappProcessRequest,SkmobiConfig skmobiConfig
			){
		
		SkmobiApiTrans skmobiApiTrans=null;
			try{
				
				skmobiApiTrans=new SkmobiApiTrans(true);
				skmobiApiTrans.setAction(MConstants.ACTION_PIN_SENT);
				skmobiApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				skmobiApiTrans.setSkMobiCampid(skmobiConfig.getCampId());
				skmobiApiTrans.setServiceId(skmobiConfig.getServiceId());				
				skmobiApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				skmobiApiTrans.setOperatoName(skmobiConfig.getOperatorName());			
			String url=SkmobiConstant
					.getUrl(skmobiConfig.getPinGenerationUrl(), null, inappProcessRequest.getMsisdn(),
							 skmobiConfig,null,inappProcessRequest.getCgToken()
							);			
			skmobiApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			skmobiApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("sendPin:::::::: "+map+" ,is true:: "+Objects.toString(map.get("status")).equals("true"));
				 //{"status":"1","msg":"OTP Sent Successfully","msisdn":"########","sCampId":"########","subscriptionContractId":"########"}
				 
				 if(map!=null&&Objects.toString(map.get("status")).equalsIgnoreCase("1")){
					 
					// inappProcessRequest.setSuccess(true);				 
					 skmobiApiTrans.setSuccess(true);	
					 String subscriptionContractId=null;
					 if(map.get("subscriptionContractId")!=null){
						 subscriptionContractId=Objects.toString(map.get("subscriptionContractId"));
					 }else{
						 subscriptionContractId=Objects.toString(map.get("sCorrelatorId"));
					 }
					 
				redisCacheService.putObjectCacheValueByEvictionMinute
				(SkmobiConstant.SKMOBI_OTP_TRANS_ID_PREFIX
						+inappProcessRequest.getMsisdn(),subscriptionContractId, 30);
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(skmobiApiTrans);
			}		
			return skmobiApiTrans;
		} 
	
	public SkmobiApiTrans validatePin(InappProcessRequest inappProcessRequest,SkmobiConfig skmobiConfig
			){
		
		SkmobiApiTrans skmobiApiTrans=null;
			try{
				
				skmobiApiTrans=new SkmobiApiTrans(true);
				skmobiApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
				skmobiApiTrans.setOperatoName(skmobiConfig.getOperatorName());				
				skmobiApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				skmobiApiTrans.setSkMobiCampid(skmobiConfig.getCampId());
				skmobiApiTrans.setServiceId(skmobiConfig.getServiceId());				
				skmobiApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				String subscriptionContractId=(String)redisCacheService.getObjectCacheValue
				(SkmobiConstant.SKMOBI_OTP_TRANS_ID_PREFIX
						+inappProcessRequest.getMsisdn());
				
			
			String url=SkmobiConstant
					.getUrl(skmobiConfig.getPinVerificationUrl(), subscriptionContractId, 
							inappProcessRequest.getMsisdn(),
							 skmobiConfig,inappProcessRequest.getPin(),null
							);			
			skmobiApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			skmobiApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("sendPin:::::::: "+map);
				 //{"status":"1","msg":"OTP Sent Successfully","msisdn":"########","sCampId":"########","subscriptionContractId":"########"}
				 
				 if(map!=null&&Objects.toString(map.get("status")).equalsIgnoreCase("1")){
					 
					// inappProcessRequest.setSuccess(true);				 
					 skmobiApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("sendPin ",ex);
			
			}finally{			
				jmsService.saveObject(skmobiApiTrans);
			}		
			return skmobiApiTrans;
		} 
	
	
	public SkmobiApiTrans isSubscribedUser(InappProcessRequest inappProcessRequest,SkmobiConfig skmobiConfig
			){
		
		SkmobiApiTrans skmobiApiTrans=null;
			try{				
				skmobiApiTrans=new SkmobiApiTrans(true);
				skmobiApiTrans.setAction(MConstants.ACTION_STATUS_CHECK);
				skmobiApiTrans.setOperatoName(skmobiConfig.getOperatorName());				
				skmobiApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
				skmobiApiTrans.setSkMobiCampid(skmobiConfig.getCampId());
				skmobiApiTrans.setServiceId(skmobiConfig.getServiceId());				
				skmobiApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				
				
			
			String url=SkmobiConstant
					.getUrl(skmobiConfig.getStatusCheckUrl(), null, 
							inappProcessRequest.getMsisdn(),skmobiConfig,
							null,null
							);			
			skmobiApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			skmobiApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			 
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200){//pin_sent
				 Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				 logger.info("isSubscribedUser:::::::: "+map);
				 //{"status":"2","msg":"You have been already subscribed to the service"}
				 //{"status":"1","msg":"User is not subscribed."}
				 //{"status":"0","msg":"Fail:Something went wrong."}
				 if(map!=null&&Objects.toString(map.get("status")).equalsIgnoreCase("2")){//					 
					 //inappProcessRequest.setSuccess(true);				 
					 skmobiApiTrans.setSuccess(true);	
				}
			 }
			
			}catch(Exception ex){
				logger.error("isSubscribedUser ",ex);
			
			}finally{			
				jmsService.saveObject(skmobiApiTrans);
			}		
			return skmobiApiTrans;
		} 
	
	
}
