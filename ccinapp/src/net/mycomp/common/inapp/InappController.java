package net.mycomp.common.inapp;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.common.jms.JMSService;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.common.service.IDaoService;
import net.mycomp.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.JsonMapper;
import net.util.MConstants;



@Controller
@RequestMapping(value={"com","audiencenest/com"})
public class InappController {

	
	 private static final Logger logger = Logger
				.getLogger(InappController.class.getName());
	 
	 @Autowired
	 private JMSService jmsService;
	 
	@Autowired
	private InappPublisherService inappPublisherService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private InappRequestFactory inappRequestFactory;
	
	@Autowired
	private JMSInappService jmsInappService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@RequestMapping(value={"pin/send"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String sendPin(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
			
		inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.SEND_PIN.action);
		if(inappProcessRequest.getMsisdn().equals("123456789")){
			Map<String,String> responseMap = new HashMap<String,String>();
			responseMap.put("STATUS", "SUCCESS");
			responseMap.put("MSG", "OTP SENT");
			responseMap.put("OTP_LEN", "4");
			inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
			return inappProcessRequest.getResponseObject().toString();
			}else {
		inappProcessRequest.setPinRequestCount(1);
		inappPublisherService.sendOtp(inappProcessRequest,modelAndView);
			}
		}catch(Exception ex){
			logger.error("sendPin "+inappProcessRequest,ex);
		}finally{
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return inappProcessRequest.getResponseObject().toString();
	} 
	
	
	@RequestMapping(value={"pin/validation"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String validatePin(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
			inappProcessRequest=inappRequestFactory.createRequestBean(request
					,InappAction.PIN_VALIDATION.action);
			if(inappProcessRequest.getMsisdn().equals("123456789") && inappProcessRequest.getPin().equals("1234")){
				Map<String,String> responseMap = new HashMap<String,String>();
				responseMap.put("STATUS", "SUCCESS");
				responseMap.put("MSG", "Otp Verify");
				responseMap.put("OTP_LEN", ""+inappProcessRequest.getPin().length());
				inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));

				SubscriberReg subscriberReg = new SubscriberReg();
				subscriberReg.setMsisdn(inappProcessRequest.getMsisdn());
				subscriberReg.setServiceId(116);
				subscriberReg.setStatus(MConstants.SUBSCRIBED);
				subscriberReg.setStatusDescp(MConstants.SUBSCRIBED_DESC);
				subscriberReg.setOperatorId(115);
				subscriberReg.setRegData(new Timestamp(System.currentTimeMillis()));
				jpaSubscriberReg.save(subscriberReg);		
				return inappProcessRequest.getResponseObject().toString();
			}else {
			inappProcessRequest.setPinValidationRequestCount(1);
			inappPublisherService.otpValidation(inappProcessRequest, modelAndView);
				}
		}catch(Exception ex){
			logger.error("validatePin "+inappProcessRequest,ex);
		}finally{	
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return inappProcessRequest.getResponseObject().toString();
		
	} 
	
	@RequestMapping(value={"pin/checksub"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String statusCheck(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
		inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.STATUS_CHECK.action);
		inappProcessRequest.setStatusCheckCount(1);
		inappPublisherService.statusCheck(inappProcessRequest, modelAndView);
		
		}catch(Exception ex){
			logger.error("statusCheck "+inappProcessRequest,ex);
		}finally{	
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return inappProcessRequest.getResponseObject().toString();
	} 
	
	
	@RequestMapping(value={"portal/url"},method={RequestMethod.GET,RequestMethod.POST})
	//@ResponseBody
	public ModelAndView portalUrl(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
		inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.PORTAL_URL.action);
		
		inappPublisherService.portalUrl(inappProcessRequest,modelAndView);
		
		}catch(Exception ex){
			logger.error("portalUrl "+inappProcessRequest,ex);
		}finally{
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return modelAndView;
	} 
	
	@RequestMapping(value={"dct"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String dct(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
		inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.DCT.action);		
		inappPublisherService.dct(inappProcessRequest);
		
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}finally{
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return inappProcessRequest.getResponseObject().toString();
	} 
	
}
