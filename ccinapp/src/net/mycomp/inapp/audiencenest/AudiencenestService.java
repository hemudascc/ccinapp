package net.mycomp.inapp.audiencenest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.jpa.repository.JPAAudiencenestConfig;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.persist.bean.SubscriberReg;

@Service("audiencenestService")
public class AudiencenestService extends AbstractInappOperatorServiceApi{

	private static final Logger logger = Logger
			.getLogger(AudiencenestService.class.getName());



	@Autowired
	private JPAAudiencenestConfig  jpaAudiencenestConfig;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private AudiencenestServiceApi audiencenestServiceApi;	
	@Value("${jdbc.db.name}")
	private String dbName;

	@PostConstruct
	public void init(){

		List<AudiencenestConfig> listInAppConfig=jpaAudiencenestConfig.findEnableAudiencenestConfig(true);		
		AudiencenestConstant.mapServiceIdToAudiencenestConfig.putAll(
				listInAppConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
				);	 
	}



	public boolean sendPin(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){

		try{

			AudiencenestConfig audiencenestConfig=AudiencenestConstant
					.mapServiceIdToAudiencenestConfig.get(inappProcessRequest
							.getServiceId());			
			AudiencenestApiTrans sendPinAudiencenestApiTrans = audiencenestServiceApi
					.sendPin(inappProcessRequest, audiencenestConfig);
			if(sendPinAudiencenestApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(true);
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
			AudiencenestConfig audiencenestConfig=AudiencenestConstant
					.mapServiceIdToAudiencenestConfig.get(inappProcessRequest
							.getServiceId());

			AudiencenestApiTrans validatePinAplayaApiTrans=audiencenestServiceApi
					.validatePin(inappProcessRequest, audiencenestConfig);
			if(validatePinAplayaApiTrans.getSuccess()){
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
			/*
			 * AudiencenestConfig audiencenestConfig = AudiencenestConstant
			 * .mapServiceIdToAudiencenestConfig.get(inappProcessRequest .getServiceId());
			 */
		logger.info("inappProcessRequest: "+inappProcessRequest);	
		 List<SubscriberReg> subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdn(inappProcessRequest.getMsisdn());
		 if(Objects.nonNull(subscriberReg) && subscriberReg.get(0).getStatus()==1) {
			 inappProcessRequest.setStatus(true);
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
			AudiencenestConfig audiencenestConfig=AudiencenestConstant
					.mapServiceIdToAudiencenestConfig.get(inappProcessRequest
							.getServiceId());
			
			if(audiencenestConfig!=null) {
				return audiencenestConfig.getPortalUrl();
			}
		}catch(Exception ex){
			logger.error("portalUrl ",ex);		
		}finally{		
		}		
		return null;
	}

}
