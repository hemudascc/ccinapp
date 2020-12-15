package net.mycomp.shemaroo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPAInappTmtConfig;
import net.jpa.repository.JPAShemarooConfig;
import net.jpa.repository.JPASkmobiConfig;
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

@Service("shemarooService")
public class ShemarooService extends AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(ShemarooService.class.getName());
	 
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private ShemarooServiceApi shemarooServiceApi;
	
	
	@Autowired
	private JMSService  jmsService;
	
	@Autowired
	private JPAShemarooConfig jpaShemarooConfig;
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){
		
		List<ShemarooConfig> listShemarooConfig=jpaShemarooConfig.findEnableShemarooConfig(true);		
		ShemarooConstant.mapServiceIdToShemarooConfig.putAll(
				listShemarooConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);	 
	}
	
	@Override
	public boolean checkBlocking(InappProcessRequest inappProcessRequest) {		
		
		Object obj=redisCacheService.getObjectCacheValue
				(ShemarooConstant.SHEMAROO_OTP_BLOCKED_PREFIX+inappProcessRequest.getMsisdn());
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
			
			ShemarooConfig shemarooConfig=ShemarooConstant
					.mapServiceIdToShemarooConfig.get(inappProcessRequest
					.getServiceId());	
			
			ShemarooApiTrans checkSubscriberSatusSkmobiApiTrans=shemarooServiceApi
					.isSubscribedUser(inappProcessRequest, shemarooConfig);
			
			if(checkSubscriberSatusSkmobiApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(false);
				return true;
			}
			
			String description=(String)redisCacheService.getObjectCacheValue
					(ShemarooConstant.SHEMAROO_OTP_TRANS_ID_PREFIX
							+inappProcessRequest.getMsisdn());
			ShemarooApiTrans sendPinSkmobiApiTrans=null;
			if(description!=null){
					String str[]=description.split(",");
			sendPinSkmobiApiTrans=shemarooServiceApi
								.resendPin(inappProcessRequest, shemarooConfig,str[0]);
			}else{	
			 sendPinSkmobiApiTrans=shemarooServiceApi
					.sendPin(inappProcessRequest, shemarooConfig);
			}
			
			if(sendPinSkmobiApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
				redisCacheService.putObjectCacheValueByEvictionMinute(ShemarooConstant.SHEMAROO_OTP_BLOCKED_PREFIX
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
			ShemarooConfig skmobiConfig=ShemarooConstant
					.mapServiceIdToShemarooConfig.get(inappProcessRequest
					.getServiceId());
			
			ShemarooApiTrans sendPinSkmobiApiTrans=shemarooServiceApi
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
			ShemarooConfig skmobiConfig=ShemarooConstant
					.mapServiceIdToShemarooConfig.get(inappProcessRequest
					.getServiceId());
			
			ShemarooApiTrans sendPinSkmobiApiTrans=shemarooServiceApi
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
			ShemarooConfig skmobiConfig=ShemarooConstant
					.mapServiceIdToShemarooConfig.get(inappProcessRequest
					.getServiceId());
			
			return skmobiConfig.getPortalUrl().replaceAll("<msisdn>", Objects.toString(
					inappProcessRequest.getMsisdn()));
		}catch(Exception ex){
			logger.error("portalUrl ",ex);		
		}finally{		
		}		
		return null;
	}
}
