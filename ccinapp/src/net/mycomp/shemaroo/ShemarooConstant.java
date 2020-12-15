package net.mycomp.shemaroo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import net.util.MUtility;

public interface ShemarooConstant {

	 static final Logger logger = Logger
				.getLogger(ShemarooConstant.class.getName());
		
	public static final String SHEMAROO_OTP_BLOCKED_PREFIX="SHEMAROO_OTP_BLOCKED";
	public static final String SHEMAROO_OTP_TRANS_ID_PREFIX="SHEMAROO_OTP_TRANS_ID_PREFIX";
	
	public static final String RESEND_PIN="RESEND_PIN";
	
	public static Map<Integer,ShemarooConfig> mapServiceIdToShemarooConfig=new HashMap<Integer,ShemarooConfig>();	
	
	public static String getUrl(String url,String subscriptionContractId
			,String customerAccountNo
			,String msisdn,
			ShemarooConfig shemarooConfig,String pin,String token
			){
		
		try{	
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<subscriptioncontractid>",
						MUtility.urlEncoding(Objects.toString(subscriptionContractId)))	
				.replaceAll("<customeaccountnumber>",
						MUtility.urlEncoding(Objects.toString(customerAccountNo)))		
				.replaceAll("<token>",Objects.toString(Objects.toString(token)))
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)));
			
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
	
}
