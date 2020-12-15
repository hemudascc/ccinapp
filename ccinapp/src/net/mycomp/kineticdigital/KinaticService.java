package net.mycomp.kineticdigital;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPAInappTmtConfig;
import net.jpa.repository.JPAKinaticConfig;
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

@Service("kinaticService")
public class KinaticService extends AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(KinaticService.class.getName());
	 
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private KinaticServiceApi kinaticServiceApi;
	
	
	@Autowired
	private JMSService  jmsService;
	
	@Autowired
	private JPAKinaticConfig jpaKinaticConfig;
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){
		
		List<KinaticConfig> listShemarooConfig=jpaKinaticConfig.findEnableKinaticConfig(true);		
		KinaticConstant.mapServiceIdToKinaticConfig.putAll(
				listShemarooConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);	 
	}
	
	@Override
	public boolean checkBlocking(InappProcessRequest inappProcessRequest) {		
		
		Object obj=redisCacheService.getObjectCacheValue
				(KinaticConstant.KINATIC_OTP_BLOCKED_PREFIX+inappProcessRequest.getMsisdn());
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
			
			KinaticConfig kinaticConfig=KinaticConstant
					.mapServiceIdToKinaticConfig.get(inappProcessRequest
					.getServiceId());	
			
		
			
			KinaticApiTrans kinaticApiTrans=kinaticServiceApi
					.sendPin(inappProcessRequest, kinaticConfig);
			
			
			if(kinaticApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
				redisCacheService.putObjectCacheValueByEvictionMinute(KinaticConstant.KINATIC_OTP_BLOCKED_PREFIX
						
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
			KinaticConfig skmobiConfig=KinaticConstant
					.mapServiceIdToKinaticConfig.get(inappProcessRequest
					.getServiceId());
			
			KinaticApiTrans sendPinSkmobiApiTrans=kinaticServiceApi
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
	
	

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		try{
			KinaticConfig skmobiConfig=KinaticConstant
					.mapServiceIdToKinaticConfig.get(inappProcessRequest
					.getServiceId());
			
			return skmobiConfig.getPortalUrl().replaceAll("<msisdn>", Objects.toString(
					inappProcessRequest.getMsisdn()));
		}catch(Exception ex){
			logger.error("portalUrl ",ex);		
		}finally{		
		}		
		return null;
	}
	
	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		try{
			KinaticConfig kinaticConfig=KinaticConstant
					.mapServiceIdToKinaticConfig.get(inappProcessRequest
					.getServiceId());
			
			KinaticApiTrans sendPinSkmobiApiTrans=kinaticServiceApi
					.checkStatus(inappProcessRequest, kinaticConfig);
			if(sendPinSkmobiApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
			}
		}catch(Exception ex){
			logger.error("validatePin ",ex);		
		}finally{		
		}		
		return true;
	}
}
