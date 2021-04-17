package net.mycomp.inapp.audiencenest;

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

@Service("audiencenestServiceApi")
public class AudiencenestServiceApi {


	private static final Logger logger = Logger
			.getLogger(AudiencenestServiceApi.class.getName());

	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private RedisCacheService redisCacheService;

	private HttpURLConnectionUtil httpURLConnectionUtil;


	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}

	public AudiencenestApiTrans sendPin(InappProcessRequest inappProcessRequest,AudiencenestConfig audiencenestConfig
			){

		AudiencenestApiTrans audiencenestApiTrans=null;
		boolean resend=false;
		try{

			audiencenestApiTrans=new AudiencenestApiTrans(true);
			audiencenestApiTrans.setAction(MConstants.ACTION_PIN_SENT);
			audiencenestApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
			audiencenestApiTrans.setAudiencenestCampid(audiencenestConfig.getCampId());
			audiencenestApiTrans.setServiceId(audiencenestConfig.getServiceId());				
			audiencenestApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
			audiencenestApiTrans.setOperatoName(audiencenestConfig.getOperatorName());
			
			if(Objects.toString(redisCacheService.getObjectCacheValue(AudiencenestConstant.AUDIENCENECT_RESEND_OTP_PREFIX+inappProcessRequest.getMsisdn())).equals("1")) {
				resend=true;
			}
			
			String url=AudiencenestConstant
					.getUrl(audiencenestConfig.getPinGenerationUrl(), resend,inappProcessRequest.getMsisdn(),
							audiencenestConfig,inappProcessRequest.getCgToken(),null
							);			
			audiencenestApiTrans.setRequest(url);  

			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			audiencenestApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200
					&& (Objects.toString(httpResponse.getResponseStr()).contains("SUCCESS")
					|| Objects.toString(httpResponse.getResponseStr()).contains("The pin code has been sent again")
					)){//pin_sent
				logger.info("send pin request msisdn="+audiencenestApiTrans.getMsisdn()+" :: response="+httpResponse.getResponseStr());
				//SUCCESS
				redisCacheService.putObjectCacheValueByEvictionMinute(AudiencenestConstant.AUDIENCENECT_RESEND_OTP_PREFIX+inappProcessRequest.getMsisdn(), "1", 5);
				inappProcessRequest.setSuccess(true);
				audiencenestApiTrans.setSuccess(true);
			}
		}catch(Exception ex){
			logger.error("sendPin ",ex);  

		}finally{			
			jmsService.saveObject(audiencenestApiTrans);
		}		
		return audiencenestApiTrans;
	} 

	public AudiencenestApiTrans validatePin(InappProcessRequest inappProcessRequest,AudiencenestConfig audiencenestConfig
			){

		AudiencenestApiTrans audiencenestApiTrans=null;
		try{

			audiencenestApiTrans=new AudiencenestApiTrans(true);
			audiencenestApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
			audiencenestApiTrans.setOperatoName(audiencenestConfig.getOperatorName());				
			audiencenestApiTrans.setMsisdn(inappProcessRequest.getMsisdn());				
			audiencenestApiTrans.setAudiencenestCampid(audiencenestConfig.getCampId());
			audiencenestApiTrans.setServiceId(audiencenestConfig.getServiceId());				
			audiencenestApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
			String url=AudiencenestConstant
					.getUrl(audiencenestConfig.getPinVerificationUrl(), false, 
							inappProcessRequest.getMsisdn(),
							audiencenestConfig,null,inappProcessRequest.getPin()
							);			
			audiencenestApiTrans.setRequest(url);

			HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
			audiencenestApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());

			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			if(httpResponse.getResponseCode()==200 
					&& Objects.toString(httpResponse.getResponseStr()).contains("SUCCESS")){//pin_sent
				//SUCCESS
					audiencenestApiTrans.setSuccess(true);	
			}

		}catch(Exception ex){
			logger.error("sendPin ",ex);

		}finally{			
			jmsService.saveObject(audiencenestApiTrans);
		}		
		return audiencenestApiTrans;
	} 
}
