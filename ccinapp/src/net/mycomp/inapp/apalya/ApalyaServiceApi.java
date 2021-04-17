package net.mycomp.inapp.apalya;

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
import net.util.MConstants;

@Service("apalyaServiceApi")
public class ApalyaServiceApi {

	private static final Logger logger = Logger
			.getLogger(ApalyaServiceApi.class.getName());

	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private RedisCacheService redisCacheService;

	private HttpURLConnectionUtil httpURLConnectionUtil;


	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}

	public ApalyaApiTrans sendPin(InappProcessRequest inappProcessRequest,ApalyaConfig apalyaConfig
			){

		ApalyaApiTrans apalyaApiTrans=null;
		boolean resend=false;
		try{

			apalyaApiTrans=new ApalyaApiTrans(true);
			apalyaApiTrans.setAction(MConstants.ACTION_PIN_SENT);
			apalyaApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
			apalyaApiTrans.setApalyaCampid(apalyaConfig.getCampId());
			apalyaApiTrans.setServiceId(apalyaConfig.getServiceId());				
			apalyaApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
			apalyaApiTrans.setOperatoName(apalyaConfig.getOperatorName());
			
			if(Objects.toString(redisCacheService.getObjectCacheValue(ApalyaConstant.APALYA_RESEND_OTP_PREFIX+inappProcessRequest.getMsisdn())).equals("1")) {
				resend=true;
			}
			
			String url=ApalyaConstant
					.getUrl(apalyaConfig.getPinGenerationUrl(), resend, inappProcessRequest.getMsisdn(),
							apalyaConfig,null,inappProcessRequest.getCgToken()
							);			
			apalyaApiTrans.setRequest(url);

			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			apalyaApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200
					&& (Objects.toString(httpResponse.getResponseStr()).contains("SUCCESS")
					|| Objects.toString(httpResponse.getResponseStr()).contains("The pin code has been sent again")
					)){//pin_sent
				// Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				logger.info("send pin request msisdn="+apalyaApiTrans.getMsisdn()+" :: response="+httpResponse.getResponseStr());
				//SUCCESS
				redisCacheService.putObjectCacheValueByEvictionMinute(ApalyaConstant.APALYA_RESEND_OTP_PREFIX+inappProcessRequest.getMsisdn(), "1", 5);
				inappProcessRequest.setSuccess(true);
				apalyaApiTrans.setSuccess(true);
			}
		}catch(Exception ex){
			logger.error("sendPin ",ex);

		}finally{			
			jmsService.saveObject(apalyaApiTrans);
		}		
		return apalyaApiTrans;
	} 

	public ApalyaApiTrans validatePin(InappProcessRequest inappProcessRequest,ApalyaConfig apalyaConfig
			){

		ApalyaApiTrans apalyaApiTrans=null;
		try{

			apalyaApiTrans=new ApalyaApiTrans(true);
			apalyaApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
			apalyaApiTrans.setOperatoName(apalyaConfig.getOperatorName());				
			apalyaApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
			apalyaApiTrans.setApalyaCampid(apalyaConfig.getCampId());
			apalyaApiTrans.setServiceId(apalyaConfig.getServiceId());				
			apalyaApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
			String url=ApalyaConstant
					.getUrl(apalyaConfig.getPinVerificationUrl(), false, 
							inappProcessRequest.getMsisdn(),
							apalyaConfig,inappProcessRequest.getPin(),null
							);			
			apalyaApiTrans.setRequest(url);

			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			apalyaApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());

			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200 
					&& Objects.toString(httpResponse.getResponseStr()).contains("SUCCESS")){//pin_sent
				//SUCCESS
					apalyaApiTrans.setSuccess(true);	
			}

		}catch(Exception ex){
			logger.error("sendPin ",ex);

		}finally{			
			jmsService.saveObject(apalyaApiTrans);
		}		
		return apalyaApiTrans;
	} 

}
