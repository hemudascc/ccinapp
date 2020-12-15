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

@Service("saintInappPublisherService")
public class SaintInappPublisherService implements IInappPublisherService{

	 private static final Logger logger = Logger
				.getLogger(SaintInappPublisherService.class.getName());
	 
	@Autowired
	private InappOperatorServiceApi inappOperatorServiceApi;
	
	@Autowired
	private AdnetworkCallbackService adnetworkCallbackService;
	
	@Override
	public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
		 Map<String,String> responseMap=new HashMap<String,String>();
		 responseMap.put("result", "false");
		 responseMap.put("message", "OTP Not Sent...");
		try{
			
		 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			 inappProcessRequest.setResponseObject("0");
			 return true;
		  }
		 
		inappOperatorServiceApi.sendPin(inappProcessRequest, modelAndView);		
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
			responseMap.put("result", "true");
			responseMap.put("message", "OTP Sent...");
		}else if(Objects.toString(inappProcessRequest.getReason())
				.equalsIgnoreCase(EnumReason.ALREADY_SUBSCRIBED.reason)){
			//{"response":"SUBSCRIBED","errorMessage":"customer already subscribe "}
			responseMap.put("result", "false");
			responseMap.put("message", "customer already subscribe ");
			inappProcessRequest.setResponseObject("0");
		}else{
			responseMap.put("result", "false");
			responseMap.put("message", "OTP Not Sent...");
			inappProcessRequest.setResponseObject("0");
		}
		
       }catch(Exception ex){
    	   logger.error("exception ",ex);
       }finally{
    	   inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
       }
		return true;
	}

	@Override
	public boolean otpValidation(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		try{
			responseMap.put("result", "false");
			responseMap.put("message", "OTP Not verified...");
			
			 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
				  throw new Exception("Blocked");
			  }
			 
		inappOperatorServiceApi.validatePin(inappProcessRequest, modelAndView);
		
		if(inappProcessRequest.isSuccess()){
			
			if(adnetworkCallbackService.
			isSendActMoreThanZeroPricePointAdnetworkCallBack(inappProcessRequest.vwCampaignDetail)){
				//inappProcessRequest.setResponseObject("1");
				inappProcessRequest.setConversionSendToAdenetwork(true);
				responseMap.put("result", "true");
				responseMap.put("message", "OTP verify...");
			}else if(Objects.toString(inappProcessRequest.getReason())
					.equalsIgnoreCase(EnumReason.ALREADY_SUBSCRIBED.reason)){
				//{"response":"SUBSCRIBED","errorMessage":"customer already subscribe "}
				responseMap.put("result", "false");
				responseMap.put("message", "customer already subscribe ");
				inappProcessRequest.setResponseObject("0");
			}else{
				inappOperatorServiceApi.addToBlock(inappProcessRequest);			
				responseMap.put("result", "false");
				responseMap.put("message", "OTP Not verified...");				
				//inappProcessRequest.setResponseObject("0");
			}
		}else{
			responseMap.put("result", "false");
			responseMap.put("message", "OTP Not verified...");
			//inappProcessRequest.setResponseObject("0");
		}
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		return true;
	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		try{
			inappProcessRequest.setResponseObject("DEACTIVATED");
			 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
				  throw new Exception("Blocked");
			  }
			 
		inappOperatorServiceApi.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("ACTIVATED");			
		}else{
			inappProcessRequest.setResponseObject("DEACTIVATED");
		}
		}catch(Exception ex){
			logger.error("Exception ",ex);
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
			responseMap.put("result", "true");
			responseMap.put("message", "Success fully unsubscribed ");
		}else{
			responseMap.put("result", "false");
			responseMap.put("message", "Fail dct proccess");
		}		
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));		
		return true;
	}
}
