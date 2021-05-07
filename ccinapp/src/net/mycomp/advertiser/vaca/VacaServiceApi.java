package net.mycomp.advertiser.vaca;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.inapp.collectcent.CollectcentConstant;
import net.mycomp.common.inapp.collectcent.CollectcentServiceConfig;
import net.mycomp.common.inapp.collectcent.InappCollectcentOtpSend;
import net.mycomp.common.inapp.collectcent.InappCollectcentStatusCheck;
import net.mycomp.common.inapp.collectcent.InappCollectcentValidation;
import net.mycomp.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.EnumReason;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("vacaServiceApi")
public class VacaServiceApi {
	
	 private static final Logger logger = Logger
				.getLogger(VacaServiceApi.class.getName());
	 
	
   @Autowired
   private RedisCacheService redisCacheService;
   
   @Autowired
   private JMSService jmsService;
   
   @Autowired
   private JPASubscriberReg jpaSubscriberReg;
     
	  
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
	
	public VacaApiTrans sendPin(InappProcessRequest inappProcessRequest
			){
		
		VacaApiTrans vacaApiTrans=null;
			try{
				logger.info("sending pin"+inappProcessRequest);
				vacaApiTrans=new VacaApiTrans(true);
				vacaApiTrans.setMsisdn(inappProcessRequest.getMsisdn());
				vacaApiTrans.setCmpId(inappProcessRequest.getCmpid());
				//vacaApiTrans.setRequestId(inappProcessRequest.getId());
				vacaApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				
				VacaConfig vacaConfig=VacaConstant
						.mapServiceIdToVacaConfig.get(inappProcessRequest
						.getServiceId());
				logger.info(" VACA service ID:: "+inappProcessRequest);
				logger.info("vacaConfig:: "+inappProcessRequest.getServiceId());
			String url=VacaConstant
					.getUrl(vacaConfig.getPinGenerationUrl(),
							inappProcessRequest.getMsisdn(),vacaConfig,
							null,""+vacaApiTrans.getTokenToCg()
					);		
			
			vacaApiTrans.setRequest(url);
			
			HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
			vacaApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			
			  Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			  inappProcessRequest.enumReason= EnumReason.findCuase(Objects.toString(map.get("errorMessage")));
			 if(map!=null&&(Objects.toString(map.get("STATUS")).equalsIgnoreCase("SUCCESS")
					 ||Objects.toString(map.get("response")).equalsIgnoreCase("SUCCESS"))){			 
				    inappProcessRequest.setSuccess(true);
					vacaApiTrans.setSuccess(true);//SendOtp(true);
					inappProcessRequest.setSendPinCount(1);
								
			  }else if(map!=null&&(Objects.toString(map.get("response")).equalsIgnoreCase("SUBSCRIBED"))){
				  inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);
			  }
			
			 
			}catch(Exception ex){
				logger.error("sendPin ",ex);		
			}finally{			
				jmsService.saveObject(vacaApiTrans);
			}		
			return vacaApiTrans;
		} 
		
		
		public VacaApiTrans validatePin(InappProcessRequest inappProcessRequest
				){
			
			VacaApiTrans vacaApiTrans=null;
			try{
				vacaApiTrans=new VacaApiTrans(true);
				vacaApiTrans.setMsisdn(inappProcessRequest.getMsisdn());
				vacaApiTrans.setCmpId(inappProcessRequest.getCmpid());
				//vacaApiTrans.setRequestId(inappProcessRequest.getId());
				vacaApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
				//vacaApiTrans.setPin(inappProcessRequest.getPin());
				//vacaApiTrans.setTrxId(inappProcessRequest.getTxid());
				
				VacaConfig vacaConfig=VacaConstant
						.mapServiceIdToVacaConfig.get(inappProcessRequest
						.getServiceId());
				
			String url=VacaConstant
					.getUrl(vacaConfig.getPinVerificationUrl(), inappProcessRequest.getMsisdn()
							, vacaConfig, inappProcessRequest.getPin(), inappProcessRequest.getCgToken());
		
			vacaApiTrans.setRequest(url);
			HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
			vacaApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
			inappProcessRequest.setAdvertiserApiRequest(url);
			inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
			inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
			
			Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			 if(map!=null&&(Objects.toString(map.get("STATUS")).equalsIgnoreCase("SUCCESS")
					 ||Objects.toString(map.get("response")).equalsIgnoreCase("SUCCESS"))){
				 
				    vacaApiTrans.setSuccess(true);
					inappProcessRequest.setSuccess(true);
					inappProcessRequest.setConversionCount(1);
					inappProcessRequest.setPinValidateAmount(vacaConfig.getPricePoint());
				logger.info("advertiser Response sucess");	
			  }else if(map!=null&&(Objects.toString(map.get("response")).equalsIgnoreCase("SUBSCRIBED"))){
				  inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);
				  logger.info("advertiser Response already subscribed");
			  }
			
				
			}catch(Exception ex){
				logger.error("sendPin ",ex);			
			}finally{
				jmsService.saveObject(vacaApiTrans);
			}		
			return vacaApiTrans;
			
		} 
		
		
		public boolean statusCheck(InappProcessRequest inappProcessRequest
				){
		   
			List<SubscriberReg> list = jpaSubscriberReg.findSubscriberRegByMsisdn(inappProcessRequest.getMsisdn());
			logger.info("checkSub:::::::::: list of subscriberreg "+list);
			SubscriberReg subscriberReg=null;
			if(list!=null&&list.size()>0){
				subscriberReg=list.get(0);
			}
			logger.info("checkSub:::::::::: subscriberReg: "+subscriberReg);
			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				inappProcessRequest.setSuccess(true);
				return true;
			}   
			return false;
			
			
//			InappCollectcentStatusCheck one97InappStatusCheck=null;
//			try{			
//				VacaConfig vacaConfig=VacaConstant
//						.mapServiceIdToVacaConfig.get(inappProcessRequest
//						.getServiceId());
//				logger.info("vaca-config"+vacaConfig==null);
//				String url=VacaConstant
//						.getUrl(vacaConfig.getStatusCheckUrl(),
//								inappProcessRequest.getMsisdn(), vacaConfig, 
//								"", inappProcessRequest.getCgToken());
//						
//				HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);				
//				inappProcessRequest.setAdvertiserApiRequest(url);
//				inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
//				inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
//				
//				if(httpResponse.getResponseCode()==200
//						&&Objects.toString(httpResponse.getResponseStr()).equalsIgnoreCase("ACTIVE")){				   
//						inappProcessRequest.setSuccess(true);					
//						return true;
//			  }
//			}catch(Exception ex){
//				logger.error("statusCheck ",ex);
//			
//			}finally{
//				jmsService.saveObject(one97InappStatusCheck);
//			}		
//			return true;
		}
		
		
		public String portalUrl(InappProcessRequest inappProcessRequest
				) {
			
			VacaConfig vacaConfig=VacaConstant
					.mapServiceIdToVacaConfig.get(inappProcessRequest
					.getServiceId());
			
			logger.info("vacaConfig:: "+vacaConfig);
			String url=VacaConstant
					.getUrl(vacaConfig.getPortalUrl(),
							inappProcessRequest.getMsisdn(),
							vacaConfig, "", "");
			inappProcessRequest.setAdvertiserApiRequest(url);
			return url;
		}

		
		public boolean dct(InappProcessRequest inappProcessRequest) {
			
			VacaConfig vacaConfig=VacaConstant
					.mapServiceIdToVacaConfig.get(inappProcessRequest
					.getServiceId());
			
			logger.info("vacaConfig:: "+vacaConfig);
			String url=VacaConstant
					.getUrl(vacaConfig.getStatusCheckUrl(),
							inappProcessRequest.getMsisdn(),
							vacaConfig, "", "");
			
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
