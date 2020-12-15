package net.mycomp.common.inapp.collectcent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import net.util.MUtility;

public interface CollectcentConstant {

	 static final Logger logger = Logger
			.getLogger(CollectcentConstant.class.getName());
	
		public static final String COLLECTCENT_OTP_PREFIX="COLLECTCENT_OTP_PREFIX";
	
	public static Map<Integer,CollectcentServiceConfig> mapServiiceIdToCollectcentServiceConfig
	=new HashMap<Integer,CollectcentServiceConfig>();	
	
	public static String getUrl(String url,String transactionId,String msisdn,String pin,String pubid,String ip
			,String token){
		try{
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<transactionid>",MUtility.urlEncoding(Objects.toString(transactionId)))				
				//.replaceAll("<token>",token)
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)))
				.replaceAll("<otp>",MUtility.urlEncoding(Objects.toString(pin)))
				.replaceAll("<pubid>",MUtility.urlEncoding(Objects.toString(pubid)))
				.replaceAll("<ip>",MUtility.urlEncoding(Objects.toString(ip)))
				.replaceAll("<token>",MUtility.urlEncoding(Objects.toString(token)))
				;
		}catch(Exception ex){
			logger.error("getUrl:: ",ex);
		}	
		return url;
	}
}
