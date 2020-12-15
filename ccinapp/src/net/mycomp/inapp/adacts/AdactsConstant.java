package net.mycomp.inapp.adacts;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import net.util.MUtility;

public interface AdactsConstant {

	 static final Logger logger = Logger
				.getLogger(AdactsConstant.class.getName());
		
	public static final String ADACTS_OTP_BLOCKED_PREFIX = "ADACTS_OTP_BLOCKED";
	public static final String ADACTS_OTP_TRANS_ID_PREFIX = "ADACTS_OTP_TRANS_ID_PREFIX";
	
	public static final String ADACTS_AFF_AFFILIATE_ID = "987";
	
	
	public static Map<Integer,AdactsConfig> mapServiceIdToAdactsConfig=new HashMap<Integer,AdactsConfig>();	
	
	public static String getUrl(String url,String subscriptionContractId,String msisdn,
			AdactsConfig adactsConfig,String pin,String token
			){
		
		try{	
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<subid>",
						MUtility.urlEncoding(Objects.toString(subscriptionContractId)))				
				.replaceAll("<token>",Objects.toString(Objects.toString(token)))
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)))
				.replaceAll("<affid>", ADACTS_AFF_AFFILIATE_ID);
			
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
	
}
