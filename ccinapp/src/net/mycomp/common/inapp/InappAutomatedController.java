package net.mycomp.common.inapp;


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

import net.jpa.repository.JPACountry;
import net.mycomp.common.service.AutomatedService;
import net.util.JsonMapper;
import net.util.MConstants;
import net.dao.ICommonDao;

@Controller
@RequestMapping(value = { "config", "audiencenest/com" })
public class InappAutomatedController {

	private static final Logger logger = Logger.getLogger(InappController.class.getName());

	@Autowired
	ICommonDao icommondao;

	@Autowired
	JPACountry jpacountry;

	@Autowired
	AutomatedService automatedService;

	@Autowired
	private InappRequestFactory inappRequestFactory;

	@RequestMapping(value = { "getcountry" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getCountry(HttpServletRequest request, ModelAndView modelAndView) {

		return automatedService.findAllCountry();  
	}

	@RequestMapping(value = { "addcountry" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addCountry(HttpServletRequest request, ModelAndView modelAndView) {
		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
		Object res = null;
		try {
			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.COUNTRY,request);
			res = automatedService.saveCountry(inappautomatedprocessrequest);
		} catch (Exception ex) {
			logger.error("addcountry:  " + ex);
		}
		return res;
	}

	@RequestMapping(value = { "getproduct" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getProduct(HttpServletRequest request, ModelAndView modelAndView) {

		return automatedService.findAllProduct();
	}

	@RequestMapping(value = { "addproduct" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addProduct(HttpServletRequest request, ModelAndView modelAndView) {
		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
		Object res = null;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		if(request.getParameter("productname") == null || request.getParameter("productname") == "") {
			responseMap.put("message", "Product name cannot be null");
			responseMap.put("status", false);
			res = JsonMapper.getObjectToJson(responseMap);
		}else {
		try {
			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.PRODUCT,request);
			res = automatedService.saveProduct(inappautomatedprocessrequest);
		} catch (Exception ex) {
			logger.error("addProduct:  " + ex);
		}
		}
		return res;

	}

	@RequestMapping(value = { "getoperators" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getOperators(HttpServletRequest request, ModelAndView modelAndView) {

		return automatedService.findAllOperator();
	}

	@RequestMapping(value = { "addoperators" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addOperators(HttpServletRequest request, ModelAndView modelAndView) {
		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
		Object res = null;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		if(request.getParameter("operatorname") == null || request.getParameter("operatorname") == "") {
			responseMap.put("message", "Operator name cannot be null");
			responseMap.put("status", false);
			res = JsonMapper.getObjectToJson(responseMap);
		}else {
		try {
			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.OPERATOR,request);
			res = automatedService.saveOperator(inappautomatedprocessrequest);
		} catch (Exception ex) {
			logger.error("addoperators:  " + ex);
		}
		}
		return res;
	}

	@RequestMapping(value = { "getservices" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getServices(HttpServletRequest request, ModelAndView modelAndView) {

		return automatedService.findAllSerivce();
	}

//	@RequestMapping(value = { "addservices" }, method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public Object addServices(HttpServletRequest request, ModelAndView modelAndView) {
//		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
//		Object res = null;
//		try {
//			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.SERVICE,request);
//			res = automatedService.saveSerivce(inappautomatedprocessrequest);
//		} catch (Exception ex) {
//			logger.error("addservices:  " + ex);
//		}
//		return res;
//	}

	@RequestMapping(value = { "getcampaign" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getCampaign(HttpServletRequest request, ModelAndView modelAndView) {

		return automatedService.findAllCampaignDetails();
	}

	@RequestMapping(value = { "addcampaign" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addCampaign(HttpServletRequest request, ModelAndView modelAndView) {
		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
		Object res = null;
		try {
			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.CAMPAIGN_DETAILS,request);
			res = automatedService.saveCampaignDetails(inappautomatedprocessrequest);
		} catch (Exception ex) {
			logger.error("campaigndetails:  " + ex);
		}
		return res;
	}

//	@RequestMapping(value = { "addadnetworkoperatorconfig" }, method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public Object addadnetworkOperatorConfig(HttpServletRequest request, ModelAndView modelAndView) {
//		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
//		Object res = null;
//		try {
//			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.ADNETWORK_OPERATOR_CONFIG,request);
//			res = automatedService.saveAdnetworkOperatorConfig(inappautomatedprocessrequest);
//		} catch (Exception ex) {
//			logger.error("addadnetworkoperatorconfig:  " + ex);
//		}
//		return res;
//	}

	@RequestMapping(value = { "addadvertiserconfig" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addAdvertiserConfig(HttpServletRequest request, ModelAndView modelAndView) {
		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
		Object res = null;
		try {
			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.ADVERTISER,request);
			res = automatedService.advertiserConfig(inappautomatedprocessrequest, modelAndView);
		} catch (Exception ex) {
			logger.error("addadvertiserconfig:  " + inappautomatedprocessrequest, ex);
		}
		return res;
	}

//	@RequestMapping(value = { "addtrafficrouting" }, method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public Object addTrafficRouting(HttpServletRequest request, ModelAndView modelAndView) {
//		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
//		Object res = null;
//		try {
//			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(MConstants.TRAFFIC_ROUTING,request);
//			res = automatedService.saveTrafficRouting(inappautomatedprocessrequest);
//		} catch (Exception ex) {
//			logger.error("TrafficRouting:  " + ex);
//		}
//		return res;
//	}
	
	@RequestMapping(value = { "getadnetwork" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getAdnetwork(HttpServletRequest request, ModelAndView modelAndView) {
		
		return automatedService.findAllAdnetwork();
	}
	
	@RequestMapping(value = { "getadvertiser" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getAdvertiser(HttpServletRequest request, ModelAndView modelAndView) {

		return automatedService.findAllAdvertiser();  
	}
	
	@RequestMapping(value = { "getserviceconfigs" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getServiceConfigs(HttpServletRequest request, ModelAndView modelAndView) {

		return automatedService.findAllServiceConfigs();
	}
}
