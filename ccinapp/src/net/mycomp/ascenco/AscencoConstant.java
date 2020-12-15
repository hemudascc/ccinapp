package net.mycomp.ascenco;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import net.util.MUtility;

public interface AscencoConstant {

	 static final Logger logger = Logger
				.getLogger(AscencoConstant.class.getName());
		
	public static final String ASCENCO_OTP_BLOCKED_PREFIX="ASCENCO_OTP_BLOCKED";
	public static final String ASCENCO_OTP_TRANS_ID_PREFIX="ASCENCO_OTP_TRANS_ID_PREFIX";
	
	public static final String RESEND_PIN="RESEND_PIN";
	
	public static Map<Integer,AscencoConfig> mapServiceIdToAscencoConfig=new HashMap<Integer,AscencoConfig>();	
	
	public static String getUrl(String url
			,String msisdn,
			AscencoConfig ascencoConfig,String pin,String token
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
