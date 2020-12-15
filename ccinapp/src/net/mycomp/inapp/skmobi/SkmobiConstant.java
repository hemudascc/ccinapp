package net.mycomp.inapp.skmobi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import net.util.MUtility;

public interface SkmobiConstant {

	 static final Logger logger = Logger
				.getLogger(SkmobiConstant.class.getName());
		
	public static final String SKMOBI_OTP_BLOCKED_PREFIX="SKMOBI_OTP_BLOCKED";
	public static final String SKMOBI_OTP_TRANS_ID_PREFIX="SKMOBI_OTP_TRANS_ID_PREFIX";
	
	public static Map<Integer,SkmobiConfig> mapServiceIdToSkmobiConfig=new HashMap<Integer,SkmobiConfig>();	
	
	public static String getUrl(String url,String subscriptionContractId,String msisdn,
			SkmobiConfig skmobiConfig,String pin,String token
			){
		
		try{	
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<subscriptioncontractid>",
						MUtility.urlEncoding(Objects.toString(subscriptionContractId)))				
				.replaceAll("<token>",Objects.toString(Objects.toString(token)))
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)));
			
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
	
}
