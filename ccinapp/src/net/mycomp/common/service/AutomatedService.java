package net.mycomp.common.service;

import org.springframework.web.servlet.ModelAndView;

import net.mycomp.common.inapp.InappAutomatedProcessRequest;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Aggregator;
import net.persist.bean.CampaignDetails;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.Service;
import net.persist.bean.TrafficRouting;

public interface AutomatedService {

	 /* In App Automated methods*/
	 
	 public Object findAllCountry();
	 public Object findAllProduct();
	 public Object findAllAggregator();
	 public Object findAllSerivce();
	 public Object findAllOperator();
	 public Object findAllCampaignDetails();
	 public Object saveCampaignDetails(InappAutomatedProcessRequest inappAutomatedProcessRequest);
	 public Object saveCountry(InappAutomatedProcessRequest inappAutomatedProcessRequest);
	 public Object saveProduct(InappAutomatedProcessRequest inappAutomatedProcessRequest);
	 public Object saveSerivce(InappAutomatedProcessRequest inappAutomatedProcessRequest);
	 public Object saveOperator(InappAutomatedProcessRequest inappAutomatedProcessRequest);
	 public Object saveAdnetworkOperatorConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest);
	 public Object advertiserConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest,ModelAndView modelAndView); 
	 public Object saveTrafficRouting(InappAutomatedProcessRequest inappAutomatedProcessRequest);
}
