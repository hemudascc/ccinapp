package net.mycomp.advertiser.vaca;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import net.util.MUtility;

public interface VacaConstant {

	 static final Logger logger = Logger
				.getLogger(VacaConstant.class.getName());
		
	public static final String VACA_OTP_BLOCKED_PREFIX="VACA_OTP_BLOCKED";
	public static final String VACA_OTP_TRANS_ID_PREFIX="VACA_OTP_TRANS_ID_PREFIX";
	
	public static final String RESEND_PIN="RESEND_PIN";
	
	public static Map<Integer,VacaConfig> mapServiceIdToVacaConfig=new HashMap<Integer,VacaConfig>();	
	
	public static String getUrl(String url
			,String msisdn,
			VacaConfig vacaConfig,String pin,String token
			){
		
		try{	
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))		
				.replaceAll("<token>",Objects.toString(Objects.toString(token)))
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)));
			
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
	
}
