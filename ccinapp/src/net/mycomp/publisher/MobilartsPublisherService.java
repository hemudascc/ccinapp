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

@Service("mobilartsPublisherService")
public class MobilartsPublisherService implements IInappPublisherService{

	 private static final Logger logger = Logger
				.getLogger(MobilartsPublisherService.class.getName());
	 
	@Autowired
	private InappOperatorServiceApi inappOperatorServiceApi;
	
	@Autowired
	private AdnetworkCallbackService adnetworkCallbackService;
	
	@Override
	public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
		 Map<String,String> responseMap=new HashMap<String,String>();
		 responseMap.put("Status", "1");
		 responseMap.put("Description", "Error");
		try{
			
		 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
			 inappProcessRequest.setResponseObject("0");
			 return true;
		  }
		 
		inappOperatorServiceApi.sendPin(inappProcessRequest, modelAndView);
		
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
			 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.SUCCCES.status);
			 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.SUCCCES.description);
		}else if(Objects.toString(inappProcessRequest.getReason())
				.equalsIgnoreCase(EnumReason.ALREADY_SUBSCRIBED.reason)){
			//{"response":"SUBSCRIBED","errorMessage":"customer already subscribe "}
			 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.USER_ALREADY_SUBSCRIBED.status);
			 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.USER_ALREADY_SUBSCRIBED.description);
			inappProcessRequest.setResponseObject("0");
		}else{
			if(inappProcessRequest.enumReason!=null){
				if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.MSISDN_NOT_FOUND.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.INVALID_MSISDN.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.INVALID_MSISDN.description);
				}else if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.INSUFFICIENT_FUNDS.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.INSUFFICIENT_FUNDS.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.INSUFFICIENT_FUNDS.description);
				}else if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.BLACKLISTED.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.description);
				}else if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.BLACKLISTED.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.description);
				}
				
			}else{
			 //responseMap.put("Status", EnumMobilartsPublisherResponseStatus.SUCCCES.status);
			Map map=JsonMapper.getJsonToObject(inappProcessRequest.getAdvertiserApiResponse(),Map.class);
			 responseMap.put("Description",Objects.toString(map.get("errorMessage")));
			}
			inappProcessRequest.setResponseObject("0");
		}
		
       }catch(Exception ex){
    	   logger.error("Exception "+inappProcessRequest,ex);
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
			 responseMap.put("Status", "1");
			 responseMap.put("Description", "Error");
			
			 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
				 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.status);
				 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.description);
				  throw new Exception("Blocked");
			  }
			 
		inappOperatorServiceApi.validatePin(inappProcessRequest, modelAndView);
		
		if(inappProcessRequest.isSuccess()){
			
			if(adnetworkCallbackService.
			isSendActMoreThanZeroPricePointAdnetworkCallBack(inappProcessRequest.vwCampaignDetail)){
				//inappProcessRequest.setResponseObject("1");
				inappProcessRequest.setConversionSendToAdenetwork(true);
				 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.SUCCCES.status);
				 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.SUCCCES.description);
			}else{
				inappOperatorServiceApi.addToBlock(inappProcessRequest);			
				 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.status);
				 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.description);
				
				//inappProcessRequest.setResponseObject("0");
			}
		}else if(Objects.toString(inappProcessRequest.getReason())
				.equalsIgnoreCase(EnumReason.ALREADY_SUBSCRIBED.reason)){
			//{"response":"SUBSCRIBED","errorMessage":"customer already subscribe "}
			 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.USER_ALREADY_SUBSCRIBED.status);
			 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.USER_ALREADY_SUBSCRIBED.description);
			inappProcessRequest.setResponseObject("0");
		}else{
			if(inappProcessRequest.enumReason!=null){
				if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.MSISDN_NOT_FOUND.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.INVALID_MSISDN.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.INVALID_MSISDN.description);
				}else if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.INSUFFICIENT_FUNDS.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.INSUFFICIENT_FUNDS.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.INSUFFICIENT_FUNDS.description);
				}else if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.BLACKLISTED.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.description);
				}else if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.BLACKLISTED.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.BLACKLISTED_MSISDN.description);
				}else if(inappProcessRequest.enumReason.reason.equalsIgnoreCase(EnumReason.PIN_VALIDATION_FAILED.reason)){
					 responseMap.put("Status", EnumMobilartsPublisherResponseStatus.INVALID_PINCODE.status);
					 responseMap.put("Description",EnumMobilartsPublisherResponseStatus.INVALID_PINCODE.description);
				}else{
					 Map map=JsonMapper.getJsonToObject(inappProcessRequest.getAdvertiserApiResponse(),Map.class);
					 responseMap.put("Description",Objects.toString(map.get("errorMessage")));					
				}
				
			}else{
			 //responseMap.put("Status", EnumMobilartsPublisherResponseStatus.SUCCCES.status);
			 Map map=JsonMapper.getJsonToObject(inappProcessRequest.getAdvertiserApiResponse(),Map.class);
			 responseMap.put("Description",Objects.toString(map.get("errorMessage")));
			}
		}
		}catch(Exception ex){
			logger.error("Exception "+inappProcessRequest,ex);
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		return true;
	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		try{
			 responseMap.put("Status",EnumMobilartsPublisherResponseStatus.ERROR.status);
			 responseMap.put("Description", EnumMobilartsPublisherResponseStatus.ERROR.description);
			
			 if(inappOperatorServiceApi.checkBlocking(inappProcessRequest)){
				 responseMap.put("Status", "3");
				 responseMap.put("Description","MSISDN not Found");
				
				  throw new Exception("Blocked");
			  }
			 
		inappOperatorServiceApi.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			 responseMap.put("Status", "0");
			 responseMap.put("Description","Active");
			
			//inappProcessRequest.setResponseObject("ACTIVE");
			
		}else{
			responseMap.put("Status", "1");
			 responseMap.put("Description","Not Active");
			//inappProcessRequest.setResponseObject("INACTIVE");
		}
		}catch(Exception ex){
			logger.error("Exception "+inappProcessRequest,ex);
		}finally{
			 
			inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
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
