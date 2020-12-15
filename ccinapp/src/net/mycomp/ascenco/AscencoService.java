package net.mycomp.ascenco;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPAAscencoConfig;
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

@Service("ascencoService")
public class AscencoService extends AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(AscencoService.class.getName());
	 
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private AscencoServiceApi ascencoServiceApi;
	
	
	@Autowired
	private JMSService  jmsService;
	
	@Autowired
	private JPAAscencoConfig jpaAscencoConfig;
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){
		
		List<AscencoConfig> listAscencoConfig=jpaAscencoConfig.findEnableAscencoConfig(true);		
		AscencoConstant.mapServiceIdToAscencoConfig.putAll(
				listAscencoConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);	 
	}
	
	@Override
	public boolean checkBlocking(InappProcessRequest inappProcessRequest) {		
		
		Object obj=redisCacheService.getObjectCacheValue
				(AscencoConstant.ASCENCO_OTP_BLOCKED_PREFIX+inappProcessRequest.getMsisdn());
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
			
			AscencoConfig ascencoConfig=AscencoConstant
					.mapServiceIdToAscencoConfig.get(inappProcessRequest
					.getServiceId());	
				
			AscencoApiTrans ascencoApiTrans=ascencoServiceApi
					.sendPin(inappProcessRequest, ascencoConfig);
			
			
			if(ascencoApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
				redisCacheService.putObjectCacheValueByEvictionMinute(AscencoConstant.ASCENCO_OTP_BLOCKED_PREFIX						
					+inappProcessRequest.getMsisdn(), "1", 2);
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
			AscencoConfig skmobiConfig=AscencoConstant
					.mapServiceIdToAscencoConfig.get(inappProcessRequest
					.getServiceId());
			
			AscencoApiTrans sendPinSkmobiApiTrans=ascencoServiceApi
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
			AscencoConfig ascencoConfig=AscencoConstant
					.mapServiceIdToAscencoConfig.get(inappProcessRequest
					.getServiceId());
			
			return ascencoConfig.getPortalUrl().replaceAll("<msisdn>", Objects.toString(
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
			AscencoConfig ascencoConfig=AscencoConstant
					.mapServiceIdToAscencoConfig.get(inappProcessRequest
					.getServiceId());
			AscencoApiTrans ascencoApiTrans=ascencoServiceApi.statusCheck(inappProcessRequest, ascencoConfig);
			return ascencoApiTrans.getSuccess();
			  
		}catch(Exception ex){
			logger.error("statusCheck ",ex);		
		}finally{		
		}		
		return false;
	}
	
}
