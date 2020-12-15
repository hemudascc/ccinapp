package net.mycomp.common.inapp;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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


}
