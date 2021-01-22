package net.mycomp.inapp.raone;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import net.util.MUtility;



public interface RaoneConstant {

	 static final Logger logger = Logger
				.getLogger(RaoneConstant.class.getName());
		
	public static final String RAONE_RESEND_OTP_PREFIX = "RAONE_RESEND_OTP";
		
	public static Map<Integer,RaoneConfig> mapServiceIdToRaoneConfig=new HashMap<Integer,RaoneConfig>();	
	
	public static String getUrl(String url,boolean resendParam,String msisdn,
			RaoneConfig raoneConfig,String pin,String token
			){
		
		try{	
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<resend>",
						MUtility.urlEncoding(Objects.toString(resendParam)))				
				.replaceAll("<token>",Objects.toString(Objects.toString(token)))
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)));
			
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
		
}
