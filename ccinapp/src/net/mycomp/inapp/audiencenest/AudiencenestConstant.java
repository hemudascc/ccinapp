package net.mycomp.inapp.audiencenest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import net.util.MUtility;

public interface AudiencenestConstant {

	 static final Logger logger = Logger
				.getLogger(AudiencenestConstant.class.getName());
		
	public static final String AUDIENCENECT_RESEND_OTP_PREFIX = "AUDIENCENECT_RESEND_OTP";
		
	public static Map<Integer,AudiencenestConfig> mapServiceIdToAudiencenestConfig=new HashMap<Integer,AudiencenestConfig>();	
	
	public static String getUrl(String url,boolean resendParam,String msisdn,
			AudiencenestConfig audiencenestConfig,String txid,String otp
			){
		
		try{	
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<resend>",
						MUtility.urlEncoding(Objects.toString(resendParam)))
				.replaceAll("<txid>",Objects.toString(Objects.toString(txid)))
				.replaceAll("<otp>",MUtility.urlEncoding(Objects.toString(otp)));
			
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
		
}
