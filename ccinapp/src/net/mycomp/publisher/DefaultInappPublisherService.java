package net.mycomp.publisher;

import net.adnetwork.callback.service.AdnetworkCallbackService;
import net.mycomp.common.inapp.InappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("defaultInappPublisherService")
public class DefaultInappPublisherService implements IInappPublisherService{

	@Autowired
	private InappOperatorServiceApi inappOperatorServiceApi;
	
	@Autowired
	private AdnetworkCallbackService adnetworkCallbackService;
	
	@Override
	public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
    
		 inappOperatorServiceApi.sendPin(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
		}else{
			inappProcessRequest.setResponseObject("0");
		}
		return true;
	}

	@Override
	public boolean otpValidation(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		inappOperatorServiceApi.validatePin(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			
					
			if(adnetworkCallbackService.
			isSendActMoreThanZeroPricePointAdnetworkCallBack(inappProcessRequest.vwCampaignDetail)){
				inappProcessRequest.setResponseObject("1");
				inappProcessRequest.setConversionSendToAdenetwork(true);
			}else{
				inappProcessRequest.setResponseObject("0");
			}
		}else{
			inappProcessRequest.setResponseObject("0");
		}
		return true;
	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		inappOperatorServiceApi.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
		}else{
			inappProcessRequest.setResponseObject("0");
		}
		
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		String url=inappOperatorServiceApi.portalUrl(inappProcessRequest, modelAndView);
		inappProcessRequest.setResponseObject(url);
		return url;
	}

	@Override
	public boolean dct(InappProcessRequest inappProcessRequest) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
