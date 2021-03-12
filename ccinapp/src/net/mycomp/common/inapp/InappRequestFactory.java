package net.mycomp.common.inapp;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.CampaignDetails;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.TrafficRouting;
import net.process.bean.CGToken;
import net.util.MConstantAdvertiser;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Service("inappRequestFactory")
public class InappRequestFactory {

	 private static final Logger logger = Logger
				.getLogger(InappRequestFactory.class.getName());
	 
	public InappProcessRequest createRequestBean(HttpServletRequest request,String action) throws Exception {

		Enumeration<String> en = request.getParameterNames();
		
		Map<String, String> requestMap = new HashMap<String, String>();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			
			requestMap.put(key, request.getParameter(key));
		}
		
		InappProcessRequest processRequest = new InappProcessRequest(true);		
		processRequest.setCmpid(MUtility.toInt(request.getParameter("cmpid"), 0));		
		processRequest.setMsisdn(request.getParameter("msisdn"));
		processRequest.setQueryStr(request.getQueryString());		
		processRequest.setTxid(request.getParameter("txid"));
		processRequest.setPin(request.getParameter("pin"));
		processRequest.setIp(request.getParameter("ip"));
		processRequest.setRequestMap(requestMap);
		CGToken cgToken=new  CGToken(System.currentTimeMillis(),processRequest.getId(),processRequest.getCmpid());
		processRequest.setCgToken(cgToken.getCGToken());
		
		processRequest.setAction(action);
		
		processRequest.vwCampaignDetail = 
				MData.mapCamapignIdToVWCampaignDetail.get(processRequest.getCmpid());
		
		return processRequest;
	}

	/* In App Automated API */

	public InappAutomatedProcessRequest createAutomatedRequestBean(int tbId,HttpServletRequest request) throws Exception {

		InappAutomatedProcessRequest automatedProcessRequest = new InappAutomatedProcessRequest();
		switch (tbId) {
		case MConstants.CAMPAIGN_DETAILS: {
			CampaignDetails campaignDetails = new CampaignDetails();
			campaignDetails.setCampaignName(request.getParameter("campaignname"));			
			automatedProcessRequest.setServiceId(MUtility.toInt(request.getParameter("serviceid"),0));
			campaignDetails.setAdNetworkId(MUtility.toInt(request.getParameter("adnetworkid"), 0));
			campaignDetails.setRegDate(new Timestamp(System.currentTimeMillis()));
			campaignDetails.setOpId(MUtility.toInt(request.getParameter("opid"), 0));
			campaignDetails.setStatus(true);
			automatedProcessRequest.setCampaignDetails(campaignDetails);	
			break;
		}
		case MConstants.COUNTRY: {
			Country country = new Country();
			country.setId(MUtility.toInt(request.getParameter("id"), 0));
			country.setName(request.getParameter("name"));
			country.setStatus(MUtility.toBoolean(request.getParameter("status"), true));
			automatedProcessRequest.setCountry(country);	
			break;
		}
		case MConstants.PRODUCT: {
			Product product = new Product();
			product.setProductName(request.getParameter("productname").trim());
			product.setStatus(true);
			automatedProcessRequest.setProduct(product);  
			break;
		}
		case MConstants.SERVICE: {
			net.persist.bean.Service service = new net.persist.bean.Service();
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
			automatedProcessRequest.setService(service);	
			break;
		}
		case MConstants.OPERATOR: {
			Operator operator = new Operator();
			operator.setOperatorName(request.getParameter("operatorname").trim());
			operator.setCountryId(1);
			operator.setStatus(true);
			operator.setAggregatorId(14); 
			automatedProcessRequest.setOperator(operator);
			break;
		} 
		case MConstants.ADVERTISER: {
			automatedProcessRequest.setAdvertiserId(MUtility.toInt(request.getParameter("advertiserid").trim(), 0));
			automatedProcessRequest.setProductId(MUtility.toInt(request.getParameter("productid").trim(), 0));
			automatedProcessRequest.setOperatorId(MUtility.toInt(request.getParameter("operatorid").trim(), 0));
			if(MConstantAdvertiser.ASCENCO == automatedProcessRequest.getAdvertiserId() && request.getParameter("authrization") != null) {
			automatedProcessRequest.setAuthorization(request.getParameter("authrization").trim());
			}if(request.getParameter("pinsendurl") !=null) {
			automatedProcessRequest.setPinSendUrl(request.getParameter("pinsendurl").trim());				
			}if(request.getParameter("resendpinurl") != null) {
			automatedProcessRequest.setResendPinUrl(request.getParameter("resendpinurl").trim());
			}if(request.getParameter("dcturl") != null) {
			automatedProcessRequest.setDctUrl(request.getParameter("dcturl").trim());
			}if(request.getParameter("statuscheckurl") != null) {
			automatedProcessRequest.setStatusCheckUrl(request.getParameter("statuscheckurl").trim());
			}if(request.getParameter("pinvalidationurl") !=null) {
			automatedProcessRequest.setPinValidationUrl(request.getParameter("pinvalidationurl").trim());
			}if(request.getParameter("portalurl") != null) {
			automatedProcessRequest.setPortalUrl(request.getParameter("portalurl").trim());		
			}if(request.getParameter("otplength")!= null) {
			automatedProcessRequest.setOtpLength(MUtility.toInt(request.getParameter("otplength").trim(),0));
			}
			automatedProcessRequest.setPricePoint(0.0);
			automatedProcessRequest.setValidity(0);
			automatedProcessRequest.setAmount(0.0);
			automatedProcessRequest.setStatus(true);
			automatedProcessRequest.setPinGenerationUrl(automatedProcessRequest.getPinSendUrl());
			automatedProcessRequest.setPinVerificationUrl(automatedProcessRequest.getPinValidationUrl());
			automatedProcessRequest.setCheckSubUrl(automatedProcessRequest.getStatusCheckUrl());
			automatedProcessRequest.setPortalUrl2(automatedProcessRequest.getPortalUrl());
			logger.info("query String : "+request.getQueryString());
			break;
			}
		}
		return automatedProcessRequest;
	}
}
