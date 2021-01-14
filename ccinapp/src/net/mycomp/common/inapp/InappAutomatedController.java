package net.mycomp.common.inapp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.jpa.repository.JPACountry;
import net.persist.bean.Aggregator;
import net.persist.bean.CampaignDetails;
import net.persist.bean.Country;
import net.persist.bean.Product;
import net.persist.bean.Service;
import net.persist.bean.TrafficRouting;
import net.persist.bean.Operator;
import net.persist.bean.AdnetworkOperatorConfig;
import net.util.MUtility;
import net.dao.ICommonDao;

import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping(value = { "config", "audiencenest/com" })
public class InappAutomatedController {

	private static final Logger logger = Logger.getLogger(InappController.class.getName());

	@Autowired
	ICommonDao icommondao;

	@Autowired
	JPACountry jpacountry;

	@Autowired
	private InappRequestFactory inappRequestFactory;

	@RequestMapping(value = { "getcountry" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getCountry(HttpServletRequest request, ModelAndView modelAndView) {

		return icommondao.findAllCountry();
	}

	@RequestMapping(value = { "addcountry" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addCountry(HttpServletRequest request, ModelAndView modelAndView) {

		Country country = null;
		Object res = null;
		try {
			country = inappRequestFactory.createCountryBean(request);
			res = icommondao.saveCountry(country);
		} catch (Exception ex) {
			logger.error("addcountry:  " + ex);
		}
		return res;
	}

	@RequestMapping(value = { "getproduct" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getProduct(HttpServletRequest request, ModelAndView modelAndView) {

		return icommondao.findAllProduct();
	}

	@RequestMapping(value = { "addproduct" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addProduct(HttpServletRequest request, ModelAndView modelAndView) {
		Object object = null;
		try {
			Product product = inappRequestFactory.createProductBean(request);
			logger.info("id:  " + product.getId()+" productName: "+product.getProductName()+" status: "+product.getStatus());
			if(product.getProductName()!=null) {
				object = icommondao.saveProduct(product);
			}
		} catch (Exception ex) {
			logger.error("addProduct:  " + ex);
		}
		return object;
	
	}

	@RequestMapping(value = { "getoperators" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Operator> getOperators(HttpServletRequest request, ModelAndView modelAndView) {

		return icommondao.findAllOperator();
	}

	@RequestMapping(value = { "addoperators" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addOperators(HttpServletRequest request, ModelAndView modelAndView) {
		
		Operator operator  = null;
		Object res = null;
		try {
			operator = inappRequestFactory.createOperatorBean(request);
			res = icommondao.saveOperator(operator);
		} catch (Exception ex) {
			logger.error("addoperators:  " + ex);
		}
		return res;
	}

	@RequestMapping(value = { "getservices" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getServices(HttpServletRequest request, ModelAndView modelAndView) {

		return icommondao.findAllSerivce();
	}

	@RequestMapping(value = { "addservices" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addServices(HttpServletRequest request, ModelAndView modelAndView) {
		Service service = new Service();
		Object res = null;
		service.setServiceId(MUtility.toInt(request.getParameter("serviceid"), 0));
		service.setServiceName(request.getParameter("servicename"));
		service.setServiceDesc(request.getParameter("servicedesc"));
		service.setOpId(MUtility.toInt(request.getParameter("opid"), 0));
		service.setAdvertiserId(MUtility.toInt(request.getParameter("advertiserid"), 0));
		service.setProductId(MUtility.toInt(request.getParameter("productid"), 0));
		service.setOtpLength(MUtility.toInt(request.getParameter("otplength"), 0));
		service.setValidity(MUtility.toInt(request.getParameter("validity"), 0));
		service.setPricePoint(MUtility.toInt(request.getParameter("pricepoint"), 0));
		service.setStatus(MUtility.toBoolean(request.getParameter("status"), false));
		try {
//			service = inappRequestFactory.createServiceBean(request);
			res = icommondao.saveSerivce(service);
		} catch (Exception ex) {
			logger.error("addservices:  " + ex);
		}
		return res;
	}

	@RequestMapping(value = { "getcampaign" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getCampaign(HttpServletRequest request, ModelAndView modelAndView) {

		return icommondao.findAllCampaignDetails();
	}

	@RequestMapping(value = { "addcampaign" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addCampaign(HttpServletRequest request, ModelAndView modelAndView) {

		CampaignDetails campaigndetails  = null;
		Object res = null;
		try {
			campaigndetails = inappRequestFactory.createCampaignDetailsBean(request);
			res = icommondao.saveCampaignDetails(campaigndetails);
		} catch (Exception ex) {
			logger.error("campaigndetails:  " + ex);
		}
		return res;


	}

	@RequestMapping(value = { "addadnetworkoperatorconfig" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addadnetworkOperatorConfig(HttpServletRequest request, ModelAndView modelAndView) {

		AdnetworkOperatorConfig adnetworkOperatorConfig = null;
		Object res = null;
		try {
			adnetworkOperatorConfig = inappRequestFactory.createAdnetworkOperatorConfigBean(request);
			res = icommondao.saveAdnetworkOperatorConfig(adnetworkOperatorConfig);
		} catch (Exception ex) {
			logger.error("addadnetworkoperatorconfig:  " + ex);
		}
		return res;

	}

	@RequestMapping(value = { "addadvertiserconfig" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addAdvertiserConfig(HttpServletRequest request, ModelAndView modelAndView) {

		InappAutomatedProcessRequest inappautomatedprocessrequest = null;
		Object res= null;

		try {

			inappautomatedprocessrequest = inappRequestFactory.createAutomatedRequestBean(request);
		
			res = icommondao.advertiserConfig(inappautomatedprocessrequest);

		} catch (Exception ex) {
			logger.error("addadvertiserconfig:  " + inappautomatedprocessrequest, ex);
		}
		return res;
	}   

	@RequestMapping(value = { "addtrafficrouting" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object addTrafficRouting(HttpServletRequest request, ModelAndView modelAndView) {

		TrafficRouting trafficRouting = null;
		Object res = null;
		try {
			trafficRouting = inappRequestFactory.createTrafficRoutingBean(request);
			res = icommondao.saveTrafficRouting(trafficRouting);
		} catch (Exception ex) {
			logger.error("TrafficRouting:  " + ex);
		}
		return res;

	}
}
