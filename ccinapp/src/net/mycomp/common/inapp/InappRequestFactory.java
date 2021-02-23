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
//			campaignDetails.setCampaignId(MUtility.toInt(request.getParameter("campaignid"), 0));			
			campaignDetails.setCampaignName(request.getParameter("campaignname"));
			campaignDetails.setAdNetworkId(MUtility.toInt(request.getParameter("adnetworkid"), 0));
			campaignDetails.setCallbackUrl(request.getParameter("callbackurl"));
			campaignDetails.setRegDate(request.getParameter("regdate"));
			campaignDetails.setOpId(MUtility.toInt(request.getParameter("opid"), 0));
			campaignDetails.setStatus(MUtility.toBoolean(request.getParameter("status"), true));
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
			product.setId(MUtility.toInt(request.getParameter("id"), 0));
			product.setProductName(request.getParameter("productname"));
			product.setStatus(MUtility.toBoolean(request.getParameter("status"),false));
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
			operator.setOperatorId(MUtility.toInt(request.getParameter("operatorid"), 0));
			operator.setOperatorName(request.getParameter("operatorname"));
			operator.setCountryId(MUtility.toInt(request.getParameter("countryid"), 0));
			operator.setStatus(Boolean.parseBoolean(request.getParameter("status")));
			operator.setAggregatorId(MUtility.toInt(request.getParameter("aggregatorid"), 0));
			automatedProcessRequest.setOperator(operator);
			break;
		}
		case MConstants.ADNETWORK_OPERATOR_CONFIG: {
			AdnetworkOperatorConfig adnetworkOperatorConfig = new AdnetworkOperatorConfig();
			adnetworkOperatorConfig.setAdnetworkOperatorConfigId(MUtility.toInt(request.getParameter("adnetworkoperatorconfigid"), 0));
			adnetworkOperatorConfig.setAdNetworkId(MUtility.toInt(request.getParameter("adnetworkid"), 0));
			adnetworkOperatorConfig.setOperatorId(MUtility.toInt(request.getParameter("operatorid"), 0));
			adnetworkOperatorConfig.setSkipNumber(MUtility.toInt(request.getParameter("skipnumber"), 0));
			adnetworkOperatorConfig.setStatus(MUtility.toBoolean(request.getParameter("status"), true));
			adnetworkOperatorConfig.setOpCpaValue(MUtility.toDouble(request.getParameter("opcpavalue"), 0));
			adnetworkOperatorConfig.setDuplicateBlockStatus(Boolean.parseBoolean(request.getParameter("duplicateblockstatus")));
			adnetworkOperatorConfig.setAdBlockStatus(MUtility.toBoolean(request.getParameter("adblockstatus"), true));
			automatedProcessRequest.setAdnetworkOperatorConfig(adnetworkOperatorConfig);
			break;
		}
		case MConstants.TRAFFIC_ROUTING: {
			TrafficRouting trafficRouting = new TrafficRouting();
//			trafficRouting.setTrafiicRoutingId(MUtility.toInt(request.getParameter("trafficroutingid"), 0));
			trafficRouting.setCampaignId(MUtility.toInt(request.getParameter("campaignid"), 0));
			trafficRouting.setServiceId(MUtility.toInt(request.getParameter("serviceid"), 0));
			trafficRouting.setPercentageOfTraffic(MUtility.toInt(request.getParameter("percentageoftraffic"), 0));
			trafficRouting.setTrafiicRoutingStatus(MUtility.toBoolean(request.getParameter("trafiicroutingstatus"), true));
			automatedProcessRequest.setTrafficRouting(trafficRouting);
			break;
		}
		case MConstants.ADVERTISER: {
			automatedProcessRequest.setAdvertiserId(MUtility.toInt(request.getParameter("advertiserId"), 0));
			automatedProcessRequest.setId(MUtility.toInt(request.getParameter("id"), 0));
			automatedProcessRequest.setServiceId(MUtility.toInt(request.getParameter("serviceid"), 0));
			automatedProcessRequest.setProductId(MUtility.toInt(request.getParameter("productid"), 0));
			automatedProcessRequest.setOperatorName(request.getParameter("operatorname"));
			automatedProcessRequest.setCampId(MUtility.toInt(request.getParameter("cmpid"), 0));
			automatedProcessRequest.setStatus(MUtility.toBoolean(request.getParameter("status"), true));
			automatedProcessRequest.setPinGenerationUrl(request.getParameter("pingenerationurl"));
			automatedProcessRequest.setPinVerificationUrl(request.getParameter("pinvarificationurl"));
			automatedProcessRequest.setStatusCheckUrl(request.getParameter("statuscheckurl"));
			automatedProcessRequest.setPortalUrl(request.getParameter("portalurl"));
			automatedProcessRequest.setPricePoint(MUtility.toDouble(request.getParameter("pricepoint"), 0));
			automatedProcessRequest.setValidity(MUtility.toInt(request.getParameter("validity"), 0));
			automatedProcessRequest.setAuthorization(request.getParameter("authrization"));
			automatedProcessRequest.setResendPinUrl(request.getParameter("resendpinurl"));
			automatedProcessRequest.setPinSendUrl(request.getParameter("pinsendurl"));
			automatedProcessRequest.setPinValidationUrl(request.getParameter("pinvalidationurl"));
			automatedProcessRequest.setDctUrl(request.getParameter("dcturl"));
			automatedProcessRequest.setAmount(MUtility.toDouble(request.getParameter("amount"), 0));
			automatedProcessRequest.setOperatorDetail(request.getParameter("operatordetail"));
			automatedProcessRequest.setCheckSubUrl(request.getParameter("checksuburl"));
			automatedProcessRequest.setPortalUrl2(request.getParameter("portalurl2"));
			break;
			}
		}
		return automatedProcessRequest;
	}
}
