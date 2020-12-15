	package net.mycomp.publisher;

import net.mycomp.common.inapp.InappProcessRequest;

import org.springframework.web.servlet.ModelAndView;

public interface IInappPublisherService {

	public boolean sendOtp(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public boolean otpValidation(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public boolean statusCheck(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public String portalUrl(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public boolean dct(InappProcessRequest inappProcessRequest);
}
