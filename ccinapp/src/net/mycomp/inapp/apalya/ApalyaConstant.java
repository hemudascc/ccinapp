package net.mycomp.inapp.apalya;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

import net.util.MUtility;

public interface ApalyaConstant {

	 static final Logger logger = Logger
				.getLogger(ApalyaConstant.class.getName());
		
	public static final String APALYA_RESEND_OTP_PREFIX = "APALYA_RESEND_OTP";
		
	public static Map<Integer,ApalyaConfig> mapServiceIdToApalyaConfig=new HashMap<Integer,ApalyaConfig>();	
	
	public static String getUrl(String url,boolean resendParam,String msisdn,
			ApalyaConfig apalyaConfig,String pin,String token
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
