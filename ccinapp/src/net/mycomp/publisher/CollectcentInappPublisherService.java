package net.mycomp.publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.adnetwork.callback.service.AdnetworkCallbackService;
import net.mycomp.common.inapp.InappController;
import net.mycomp.common.inapp.InappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.util.EnumReason;
import net.util.JsonMapper;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Service("collectcentInappPublisherService")
public class CollectcentInappPublisherService implements IInappPublisherService{

	 private static final Logger logger = Logger
				.getLogger(CollectcentInappPublisherService.class.getName());
	 
	@Autowired
	private InappOperatorServiceApi inappOperatorServiceApi;
	
	@Autowired
	private AdnetworkCallbackService adnetworkCallbackService;
	
	@Override
	public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
        
		int otpLength=4; 
		 Map<String,String> responseMap=new HashMap<String,String>();
		 responseMap.put("STATUS", "FAIL");
	     responseMap.put("MSG", "OTP Not Sent...");
	     responseMap.put("OTP_LEN", ""+otpLength);
	     
		try{
		
		
			
		 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			 inappProcessRequest.setResponseObject("0");
			 return true;
		  }
		 
		 inappOperatorServiceApi.sendPin(inappProcessRequest, modelAndView);
		 net.persist.bean.Service service=MData.mapServiceIdToService.get(inappProcessRequest.getServiceId());
		 if(service!=null){
			 otpLength=service.getOtpLength();
		 }
		 
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
			responseMap.put("STATUS", "SUCCESS");
			responseMap.put("MSG", "OTP SENT");
			 responseMap.put("OTP_LEN", ""+otpLength);
		}else if(Objects.toString(inappProcessRequest.getReason())
				.equalsIgnoreCase(EnumReason.ALREADY_SUBSCRIBED.reason)){
			//{"response":"SUBSCRIBED","errorMessage":"customer already subscribe "}
			responseMap.put("STATUS", "ALREADY_ACTIVE");
			responseMap.put("MSG", "Already Activer User");
			 responseMap.put("OTP_LEN", ""+otpLength);
			inappProcessRequest.setResponseObject("0");
			
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "OTP Not SENT");
			 responseMap.put("OTP_LEN", ""+otpLength);
			inappProcessRequest.setResponseObject("0");
		}
	
		}catch(Exception ex){
			logger.error("Exception ",ex);
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
			int otpLength=4; 
			
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "Otp Not Verify");
			 responseMap.put("OTP_LEN", ""+otpLength);
			 
		 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			  throw new Exception("Blocked");
		  }
		 
		inappOperatorServiceApi.validatePin(inappProcessRequest, modelAndView);
		net.persist.bean.Service service=MData.mapServiceIdToService.get(inappProcessRequest.getServiceId());
		 if(service!=null){
			 otpLength=service.getOtpLength();
		 }
		 logger.info("inappProcessRequest success Status:  "+inappProcessRequest.isSuccess());
		if(inappProcessRequest.isSuccess()){
			
			if(adnetworkCallbackService.
			isSendActMoreThanZeroPricePointAdnetworkCallBack(inappProcessRequest.vwCampaignDetail)){
				logger.info("Collectcent Publisher Success");
				//inappProcessRequest.setResponseObject("1");
				inappProcessRequest.setConversionSendToAdenetwork(true);
				responseMap.put("STATUS", "SUCCESS");
				responseMap.put("MSG", "Otp Verify");
				 responseMap.put("OTP_LEN", ""+otpLength);
			}else{
				logger.info("Collectcent Publisher fail");
				inappOperatorServiceApi.addToBlock(inappProcessRequest);
				responseMap.put("STATUS", "FAIL");
				responseMap.put("MSG", "Otp Not Verify");
				 responseMap.put("OTP_LEN", ""+otpLength);
				//inappProcessRequest.setResponseObject("0");
			}
		}else{
			logger.info("Collectcent Publisher Status fail");
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "Otp Not Verify");
			 responseMap.put("OTP_LEN", ""+otpLength);
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
		inappProcessRequest.setResponseObject("0");
		if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			  throw new Exception("Blocked");
		  }
		
		inappOperatorServiceApi.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
		}else{
			inappProcessRequest.setResponseObject("0");
		}
		}catch(Exception ex){
			
		}
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		String url=inappOperatorServiceApi.portalUrl(inappProcessRequest, modelAndView);
		if(url!=null){
			responseMap.put("STATUS", "SUCCESS");
			responseMap.put("MSG", url);
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "In Active User");
		}		
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		modelAndView.setView(new RedirectView(url));
		return url;
	}

	@Override
	public boolean dct(InappProcessRequest inappProcessRequest) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
