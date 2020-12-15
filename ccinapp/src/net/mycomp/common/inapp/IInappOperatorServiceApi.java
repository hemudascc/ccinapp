package net.mycomp.common.inapp;

import org.springframework.web.servlet.ModelAndView;

public interface IInappOperatorServiceApi {

	public boolean sendPin(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public boolean validatePin(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public boolean statusCheck(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public String portalUrl(InappProcessRequest inappProcessRequest,ModelAndView modelAndView);
	public boolean checkBlocking(InappProcessRequest inappProcessRequest);
	public boolean addToBlock(InappProcessRequest inappProcessRequest);
	public boolean dct(InappProcessRequest inappProcessRequest);
	
}
