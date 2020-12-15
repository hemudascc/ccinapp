package net.mycomp.common.inapp.one97;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import net.util.MUtility;

public interface InappOne97Constant {

	 static final Logger logger = Logger
			.getLogger(InappOne97Constant.class.getName());
	
		public static final String ONE97_OTP_PREFIX="ONE97_OTP_PREFIX";
	
	public static Map<Integer,InAppOne97Config> mapServiiceIdToOne97InAppConfig=new HashMap<Integer,InAppOne97Config>();	
	
	public static String[] parse(String str){
		
		try{
			return str.split("\\|");
			
			
		}catch(Exception ex){
			logger.error("parse:: ",ex);
		}	
		return null;
	}
	
	public static String getUrl(String url,String transactionId,String msisdn,String pin,String pubid,String ip
			,String token){
		
		try{
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<transactionid>",MUtility.urlEncoding(Objects.toString(transactionId)))				
				//.replaceAll("<token>",token)
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)))
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
