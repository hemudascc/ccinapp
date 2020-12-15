package net.mycomp.common.inapp.tmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

import net.util.MUtility;

public interface InappTmtConstant {

	 static final Logger logger = Logger
			.getLogger(InappTmtConstant.class.getName());
	
	
	public static Map<Integer,InAppTmtConfig> mapServiiceIdToTmtInAppConfig=new HashMap<Integer,InAppTmtConfig>();	
//	public static AtomicInteger otpSendIdAtomicInteger=new AtomicInteger(0);
//	public static AtomicInteger otpValidationIdAtomicInteger=new AtomicInteger(0);
		
	
	
	
	public static String[] parse(String str){
		
		try{
			return str.split("\\|");
			
			
		}catch(Exception ex){
			logger.error("parse:: ",ex);
		}	
		return null;
	}
	
	public static String getUrl(String url,String transactionId,String msisdn,
			InAppTmtConfig inAppConfig,String pin,String token
			){
		
		try{			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(Objects.toString(msisdn)))
				.replaceAll("<transactionid>",MUtility.urlEncoding(Objects.toString(transactionId)))				
				.replaceAll("<token>",Objects.toString(Objects.toString(token)))
				.replaceAll("<pin>",MUtility.urlEncoding(Objects.toString(pin)))
				;
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
	
	public static void main(String arg[]){
		System.out.println(Objects.toString(null)==null);
	}
	
}
