package net.mycomp.publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.adnetwork.callback.service.AdnetworkCallbackService;
import net.mycomp.common.inapp.InappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.util.EnumReason;
import net.util.JsonMapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Service("toeInappPublisherService")
public class TOEInappPublisherService implements IInappPublisherService{

	 private static final Logger logger = Logger
				.getLogger(TOEInappPublisherService.class.getName());
	 
	@Autowired
	private InappOperatorServiceApi inappOperatorServiceApi;
	
	@Autowired
	private AdnetworkCallbackService adnetworkCallbackService;
	
	@Override
	public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
       try{
		 
		 Map<String,String> responseMap=new HashMap<String,String>();
		 responseMap.put("response", "FAIL");
		 responseMap.put("errorMessage", "OTP Not Sent...");
			
		 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			  throw new Exception("Blocked ");
		  }
		inappOperatorServiceApi.sendPin(inappProcessRequest, modelAndView);
		inappProcessRequest.setSuccess(true);
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
			responseMap.put("response", "SUCCESS");
			responseMap.put("errorMessage", "OTP Sent...");
		}else if(Objects.toString(inappProcessRequest.getReason())
				.equalsIgnoreCase(EnumReason.ALREADY_SUBSCRIBED.reason)){
			//{"response":"SUBSCRIBED","errorMessage":"customer already subscribe "}
			responseMap.put("response", "SUBSCRIBED");
			responseMap.put("errorMessage", "customer already subscribe ");
			inappProcessRequest.setResponseObject("0");
		}else{
			responseMap.put("response", "FAIL");
			responseMap.put("errorMessage", "OTP Not Sent...");
			inappProcessRequest.setResponseObject("0");
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
       }catch(Exception ex){
    	   logger.error("Exception "+inappProcessRequest,ex);
       }
		return true;
	}

	@Override
	public boolean otpValidation(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		try{
			responseMap.put("response", "FAIL");
			responseMap.put("errorMessage", "OTP Not verified...1");			
			logger.info("otpValidation:: "+inappProcessRequest);
			
		if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			logger.info("otpValidation:: "+inappProcessRequest+" BLOCKED ");
				  throw new Exception("Blocked");
			  }
			 
		inappOperatorServiceApi.validatePin(inappProcessRequest, modelAndView);
		//inappProcessRequest.setSuccess(true);
		logger.info("otpValidation:: "+inappProcessRequest+" After API call ");
		if(inappProcessRequest.isSuccess()){
			boolean send=adnetworkCallbackService.
					isSendActMoreThanZeroPricePointAdnetworkCallBack(inappProcessRequest.vwCampaignDetail);
			logger.info("otpValidation:: "+inappProcessRequest+" send::  "+send);
			if(send){
				
				//inappProcessRequest.setResponseObject("1");
				inappProcessRequest.setConversionSendToAdenetwork(true);
				responseMap.put("response", "SUCCESS");
				responseMap.put("errorMessage", "OTP verify...");
			}else{
				logger.info("otpValidation:: "+inappProcessRequest+" not sed::  ");
				inappOperatorServiceApi.addToBlock(inappProcessRequest);			
				responseMap.put("response", "FAIL");
				responseMap.put("errorMessage", "OTP Not verified....2");
				
				//inappProcessRequest.setResponseObject("0");
			}
		}else if(Objects.toString(inappProcessRequest.getReason())
				.equalsIgnoreCase(EnumReason.ALREADY_SUBSCRIBED.reason)){
			//{"response":"SUBSCRIBED","errorMessage":"customer already subscribe "}
			responseMap.put("response", "SUBSCRIBED");
			responseMap.put("errorMessage", "customer already subscribe ");
			inappProcessRequest.setResponseObject("0");
		}else{
			logger.info("otpValidation:: "+inappProcessRequest+" : send pin api failed ");
			responseMap.put("response", "FAIL");
			responseMap.put("errorMessage", "OTP Not verified.....3");
			//inappProcessRequest.setResponseObject("0");
		}
		}catch(Exception ex){
			responseMap.put("response", "FAIL");
			responseMap.put("errorMessage", "OTP Not verified.....4");
			logger.error("Exception "+inappProcessRequest,ex);
		}finally{
			logger.info("otpValidation:: "+inappProcessRequest+" : responseMap "+responseMap);
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		return true;
	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		try{
			inappProcessRequest.setResponseObject("INACTIVE");
			 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
				  throw new Exception("Blocked");
			  }
			 
		inappOperatorServiceApi.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("ACTIVE");
			
		}else{
			inappProcessRequest.setResponseObject("INACTIVE");
		}
		}catch(Exception ex){
			logger.error("Exception "+inappProcessRequest,ex);
		}
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		String url=inappOperatorServiceApi.portalUrl(inappProcessRequest, modelAndView);
		if(url!=null){
			responseMap.put("response", "SUCCESS");
			responseMap.put("errorMessage", url);
		}else{
			responseMap.put("response", "FAIL");
			responseMap.put("errorMessage", "In Active User");
		}		
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		modelAndView.setView(new RedirectView(url));
		return url;
	}

	@Override
	public boolean dct(InappProcessRequest inappProcessRequest) {
		
		Map<String,String> responseMap=new HashMap<String,String>();
		boolean success=inappOperatorServiceApi.dct(inappProcessRequest);
		if(success){
			responseMap.put("response", "SUCCESS");
			responseMap.put("errorMessage", "Success fully unsubscribed ");
		}else{
			responseMap.put("response", "FAIL");
			responseMap.put("errorMessage", "Fail dct proccess");
		}		
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));		
		return true;
	}
}
