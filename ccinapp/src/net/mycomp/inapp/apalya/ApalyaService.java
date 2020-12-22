package net.mycomp.inapp.apalya;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.jpa.repository.JPAApalyaConfig;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.persist.bean.SubscriberReg;

@Service("apalyaService")
public class ApalyaService extends AbstractInappOperatorServiceApi{

	private static final Logger logger = Logger
			.getLogger(ApalyaService.class.getName());



	@Autowired
	private JPAApalyaConfig  jpaApalyaConfig;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private ApalyaServiceApi apalyaServiceApi;	
	@Value("${jdbc.db.name}")
	private String dbName;

	@PostConstruct
	public void init(){

		List<ApalyaConfig> listInAppConfig=jpaApalyaConfig.findEnableApalyaConfig(true);		
		ApalyaConstant.mapServiceIdToApalyaConfig.putAll(
				listInAppConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
				);	 
	}



	public boolean sendPin(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){

		try{

			ApalyaConfig apalyaConfig=ApalyaConstant
					.mapServiceIdToApalyaConfig.get(inappProcessRequest
							.getServiceId());			
			ApalyaApiTrans sendPinApalyaApiTrans = apalyaServiceApi
					.sendPin(inappProcessRequest, apalyaConfig);
			if(sendPinApalyaApiTrans.getSuccess()){
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
			ApalyaConfig apalyaConfig=ApalyaConstant
					.mapServiceIdToApalyaConfig.get(inappProcessRequest
							.getServiceId());

			ApalyaApiTrans validatePinAplayaApiTrans=apalyaServiceApi
					.validatePin(inappProcessRequest, apalyaConfig);
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
			 * ApalyaConfig apalyaConfig = ApalyaConstant
			 * .mapServiceIdToApalyaConfig.get(inappProcessRequest .getServiceId());
			 */
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
			ApalyaConfig apalyaConfig=ApalyaConstant
					.mapServiceIdToApalyaConfig.get(inappProcessRequest
							.getServiceId());
			
			if(apalyaConfig!=null) {
				return apalyaConfig.getPortalUrl();
			}
		}catch(Exception ex){
			logger.error("portalUrl ",ex);		
		}finally{		
		}		
		return null;
	}
}
