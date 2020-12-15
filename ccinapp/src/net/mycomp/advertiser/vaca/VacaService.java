package net.mycomp.advertiser.vaca;

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
import net.jpa.repository.JPAVacaConfig;
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

@Service("vacaService")
public class VacaService extends AbstractInappOperatorServiceApi{

	 private static final Logger logger = Logger
				.getLogger(VacaService.class.getName());
	 
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private VacaServiceApi vacaServiceApi;
	
	
	@Autowired
	private JMSService  jmsService;
	
	@Autowired
	private JPAVacaConfig jpaVacaConfig;
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){
		
		List<VacaConfig> listVacaConfig=jpaVacaConfig.findEnableVacaConfig(true);
		logger.info("VACA-CONFIG COUNT"+listVacaConfig.size());
		VacaConstant.mapServiceIdToVacaConfig.putAll(
				listVacaConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);
		logger.info("VACA-CONFIG "+VacaConstant.mapServiceIdToVacaConfig.get(94));
	}
	
	@Override
	public boolean checkBlocking(InappProcessRequest inappProcessRequest) {		
		
		Object obj=redisCacheService.getObjectCacheValue
				(VacaConstant.VACA_OTP_BLOCKED_PREFIX+inappProcessRequest.getMsisdn());
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
			
			
				logger.info("sending pin");
			VacaApiTrans ascencoApiTrans=vacaServiceApi.sendPin(inappProcessRequest);
			
			
			if(ascencoApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
				redisCacheService.putObjectCacheValueByEvictionMinute(VacaConstant.VACA_OTP_BLOCKED_PREFIX						
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
			VacaConfig skmobiConfig=VacaConstant
					.mapServiceIdToVacaConfig.get(inappProcessRequest
					.getServiceId());
			
			VacaApiTrans sendPinSkmobiApiTrans=vacaServiceApi
					.validatePin(inappProcessRequest);
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
			VacaConfig ascencoConfig=VacaConstant
					.mapServiceIdToVacaConfig.get(inappProcessRequest
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
			
			return vacaServiceApi.statusCheck(inappProcessRequest);
			//return ascencoApiTrans.getSuccess();
			  
		}catch(Exception ex){
			logger.error("statusCheck ",ex);		
		}finally{		
		}		
		return false;
	}
	
}
