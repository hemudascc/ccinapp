package net.mycomp.inapp.adacts;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.common.jms.JMSService;
import net.jpa.repository.JPAAdactsConfig;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.service.IDaoService;
import net.mycomp.common.service.RedisCacheService;

@Service("adactsService")
public class AdactsService extends AbstractInappOperatorServiceApi{

	private static final Logger logger = Logger
			.getLogger(AdactsService.class.getName());


	@Autowired
	private RedisCacheService redisCacheService;

	@Autowired
	private IDaoService daoService;

	@Autowired
	private JPAAdactsConfig  jpaAdactsConfig;

	@Autowired
	private JMSService  jmsService;

	@Autowired
	private AdactsServiceApi adactsServiceApi;	

	@Value("${jdbc.db.name}")
	private String dbName;

	@PostConstruct
	public void init(){

		List<AdactsConfig> listInAppConfig=jpaAdactsConfig.findEnableAdactsConfig(true);		
		AdactsConstant.mapServiceIdToAdactsConfig.putAll(
				listInAppConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
				);	 
	}



	public boolean sendPin(InappProcessRequest inappProcessRequest
			,ModelAndView modelAndView){

		try{

			AdactsConfig adactsConfig=AdactsConstant
					.mapServiceIdToAdactsConfig.get(inappProcessRequest
							.getServiceId());			
			AdactsApiTrans checkSubscriberSatusAdactsApiTrans=adactsServiceApi.isSubscribedUser(inappProcessRequest, adactsConfig);

			if(checkSubscriberSatusAdactsApiTrans.getSuccess()){
				inappProcessRequest.setSuccess(false);
				return true;
			}
			AdactsApiTrans sendPinAdactsApiTrans = adactsServiceApi
					.sendPin(inappProcessRequest, adactsConfig);
			if(sendPinAdactsApiTrans.getSuccess()){
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
			AdactsConfig adactsConfig=AdactsConstant
					.mapServiceIdToAdactsConfig.get(inappProcessRequest
							.getServiceId());

			AdactsApiTrans sendPinAdactsApiTrans=adactsServiceApi
					.validatePin(inappProcessRequest, adactsConfig);
			if(sendPinAdactsApiTrans.getSuccess()){
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
			AdactsConfig adactsConfig=AdactsConstant
					.mapServiceIdToAdactsConfig.get(inappProcessRequest
							.getServiceId());

			AdactsApiTrans sendPinAdactsApiTrans=adactsServiceApi
					.isSubscribedUser(inappProcessRequest, adactsConfig);
			if(sendPinAdactsApiTrans.getSuccess()){
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
			AdactsConfig adactsConfig=AdactsConstant
					.mapServiceIdToAdactsConfig.get(inappProcessRequest
							.getServiceId());

			AdactsApiTrans sendPinAdactsApiTrans=adactsServiceApi
					.isSubscribedUser(inappProcessRequest, adactsConfig);
			if(sendPinAdactsApiTrans.getSuccess()){
				return sendPinAdactsApiTrans.getPortalURL();
			}
		}catch(Exception ex){
			logger.error("portalUrl ",ex);		
		}finally{		
		}		
		return null;
	}
}
