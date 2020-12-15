package net.mycomp.kineticdigital;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import net.util.MUtility;

public interface KinaticConstant {

	 static final Logger logger = Logger
				.getLogger(KinaticConstant.class.getName());
		
	public static final String KINATIC_OTP_BLOCKED_PREFIX="KINATIC_OTP_BLOCKED";
	public static final String KINATIC_OTP_TRANS_ID_PREFIX="KINATIC_OTP_TRANS_ID_PREFIX";
	
	public static final String RESEND_PIN="RESEND_PIN";
	
	public static Map<Integer,KinaticConfig> mapServiceIdToKinaticConfig=new HashMap<Integer,KinaticConfig>();	
	
	public static String getUrl(String url
			,String msisdn,
			KinaticConfig kinaticConfig,String pin,String token
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
