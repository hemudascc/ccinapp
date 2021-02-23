package net.mycomp.publisher;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.adnetwork.callback.service.AdnetworkCallbackService;
import net.mycomp.common.inapp.InappController;
import net.mycomp.common.inapp.InappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.util.JsonMapper;

@Service("sarasMediaInappPublisherService")
public class SarasMediaInappPublisherService implements IInappPublisherService{

	 private static final Logger logger = Logger
				.getLogger(InappController.class.getName());
	 
	@Autowired
	private InappOperatorServiceApi inappOperatorServiceApi;
	
	@Autowired
	private AdnetworkCallbackService adnetworkCallbackService;
	

	
	@Override
	public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
		 Map<String,String> responseMap=new HashMap<String,String>();
			responseMap.put("STATUS", "1");
			responseMap.put("Description", "OTP Not Sent...");
		try{
			
		  if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			  throw new Exception("Blocked");
		  }	
			
		 inappOperatorServiceApi.sendPin(inappProcessRequest, modelAndView);
	
		if(inappProcessRequest.isSuccess()){
			
			inappProcessRequest.setResponseObject("1");
			responseMap.put("Status", "0");
			responseMap.put("Description", "ok");
		}else{
			responseMap.put("STATUS", "1");
			responseMap.put("Description", "OTP Not Sent...");
			inappProcessRequest.setResponseObject("0");
		}
		
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		return true;
	}

	@Override
	public boolean otpValidation(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		try{
		responseMap.put("STATUS", "1");
		responseMap.put("Description", "OTP Not verified...");
		 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			  throw new Exception("Blocked");
		  }
		 
		inappOperatorServiceApi.validatePin(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			
			if(adnetworkCallbackService.
			isSendActMoreThanZeroPricePointAdnetworkCallBack(inappProcessRequest.vwCampaignDetail)){
				//inappProcessRequest.setResponseObject("1");
				inappProcessRequest.setConversionSendToAdenetwork(true);
				responseMap.put("Status", "0");
				responseMap.put("Description", "ok");
			}else{
				inappOperatorServiceApi.addToBlock(inappProcessRequest);
				responseMap.put("STATUS", "1");
				responseMap.put("Description", "OTP Not verified...");
				//inappProcessRequest.setResponseObject("0");
			}
		}else{
			responseMap.put("STATUS", "1");
			responseMap.put("Description", "OTP Not verified...");
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
		
		Map<String,String> responseMap=new HashMap<String,String>();
		try{
			responseMap.put("Status", "1");
			responseMap.put("Description", "Not Active");
			
			 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
				  throw new Exception("Blocked");
			  }
			 
		inappOperatorServiceApi.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			responseMap.put("Status", "0");
			responseMap.put("Description", "Active");
			inappProcessRequest.setResponseObject("1");
		}else{
			responseMap.put("Status", "1");
			responseMap.put("Description", "Not Active");
			inappProcessRequest.setResponseObject("0");
		}
		}catch(Exception ex){
			logger.error("Exception ",ex);
			
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		Map<String,String> responseMap=new HashMap<String,String>();
		String url=null;
		try{
		 url=inappOperatorServiceApi.portalUrl(inappProcessRequest, modelAndView);
		
		 
		if(url!=null){
			responseMap.put("STATUS", "SUCCESS");
			responseMap.put("MSG", url);
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "In Active User");
		}		
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		modelAndView.setView(new RedirectView(url));
		}catch(Exception ex){
			logger.error("portalUrl ",ex );
		}
		return url;
	}

	@Override
	public boolean dct(InappProcessRequest inappProcessRequest) {
		// TODO Auto-generated method stub
		return false;
	}

}
