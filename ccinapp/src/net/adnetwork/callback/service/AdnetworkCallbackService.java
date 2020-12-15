/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adnetwork.callback.service;

import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.VWCampaignDetail;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("adnetworkCallbackService")
public class AdnetworkCallbackService {

	private static final Logger logger = Logger.getLogger(AdnetworkCallbackService.class);

	public boolean isSendActMoreThanZeroPricePointAdnetworkCallBack(VWCampaignDetail vwserviceCampaignDetail
			) {		
		try{
		AdnetworkOperatorConfig adnetworkOperatorConfig = MData.mapAdnetworkOpConfig
				.get(vwserviceCampaignDetail.getOpId())
				.get(vwserviceCampaignDetail.getAdNetworkId());	
		
		boolean isSendToAdnetwork=!(adnetworkOperatorConfig.atomicActSkipNumber.
				 getAndUpdate(2, n->n>=adnetworkOperatorConfig.atomicActSkipNumber.get(1)?1:n+1)
				 <=adnetworkOperatorConfig.atomicActSkipNumber.get(0));
		return isSendToAdnetwork;
		}catch(Exception ex){
			
		}
		return false;
	}

	public static void main(String arg[]){
		
	}
	
	}
