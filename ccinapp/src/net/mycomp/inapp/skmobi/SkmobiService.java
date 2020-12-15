package net.mycomp.inapp.skmobi;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.common.jms.JMSService;
import net.jpa.repository.JPASkmobiConfig;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.service.IDaoService;
import net.mycomp.common.service.RedisCacheService;

@Service("skmobiService")
public class SkmobiService extends AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(SkmobiService.class.getName());
	 
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASkmobiConfig  jpaSkmobiConfig;
	
	@Autowired
	private JMSService  jmsService;
	
	@Autowired
	private SkmobiServiceApi skmobiServiceApi;	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){
		
		List<SkmobiConfig> listInAppConfig=jpaSkmobiConfig.findEnableSkmobiConfig(true);		
		SkmobiConstant.mapServiceIdToSkmobiConfig.putAll(
				listInAppConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);	 
	}
	
	@Override
	public boolean checkBlocking(InappProcessRequest inappProcessRequest) {		
		
		Object obj=redisCacheService.getObjectCacheValue
				(SkmobiConstant.SKMOBI_OTP_BLOCKED_PREFIX+inappProcessRequest.getMsisdn());
		if(obj!=null){
			return true;
		}
		return false;
	}
	
	
public boolean sendPin(InappProcessRequest inappProcessRequest
		,ModelAndView modelAndView){
	
		try{
			
			if(checkBlocking(inappProcessRequest)){
				inappProcessRequest.setSuccess(false);
				inappProcessRequest
				.setAdvertiserApiComment("repeated pin send blocked for some time as per advertiser ");
				return true;
			}
			
			SkmobiConfig skmobiConfig=SkmobiConstant
					.mapServiceIdToSkmobiConfig.get(inappProcessRequest
					.getServiceId());			
			SkmobiApiTrans checkSubscriberSatusSkmobiApiTrans=skmobiServiceApi.isSubscribedUser(inappProcessRequest, skmobiConfig);
			
			if(checkSubscriberSatusSkmobiApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(false);
				return true;
			}
			SkmobiApiTrans sendPinSkmobiApiTrans=skmobiServiceApi
					.sendPin(inappProcessRequest, skmobiConfig);
			if(sendPinSkmobiApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
				redisCacheService.putObjectCacheValueByEvictionMinute(SkmobiConstant.SKMOBI_OTP_BLOCKED_PREFIX
						+inappProcessRequest.getMsisdn(), "1", 5);
			}
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		
		}finally{		
		}		
		return true;
	} 
	
	
	public boolean validatePin(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
		

		try{
			SkmobiConfig skmobiConfig=SkmobiConstant
					.mapServiceIdToSkmobiConfig.get(inappProcessRequest
					.getServiceId());
			
			SkmobiApiTrans sendPinSkmobiApiTrans=skmobiServiceApi
					.validatePin(inappProcessRequest, skmobiConfig);
			if(sendPinSkmobiApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
			}
		}catch(Exception ex){
			logger.error("validatePin ",ex);		
		}finally{		
		}		
		return true;
		
	} 
	
	public boolean statusCheck(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){
	   
		try{
			SkmobiConfig skmobiConfig=SkmobiConstant
					.mapServiceIdToSkmobiConfig.get(inappProcessRequest
					.getServiceId());
			
			SkmobiApiTrans sendPinSkmobiApiTrans=skmobiServiceApi
					.isSubscribedUser(inappProcessRequest, skmobiConfig);
			if(sendPinSkmobiApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
			}
		}catch(Exception ex){
			logger.error("statusCheck ",ex);		
		}finally{		
		}		
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		try{
			SkmobiConfig skmobiConfig=SkmobiConstant
					.mapServiceIdToSkmobiConfig.get(inappProcessRequest
					.getServiceId());
			
			return skmobiConfig.getPortalUrl();
		}catch(Exception ex){
			logger.error("portalUrl ",ex);		
		}finally{		
		}		
		return null;
	}
}
