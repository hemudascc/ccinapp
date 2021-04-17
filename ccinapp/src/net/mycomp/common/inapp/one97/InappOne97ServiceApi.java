package net.mycomp.common.inapp.one97;

import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPAInappTmtConfig;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.service.IDaoService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("inappOne97ServiceApi")
public class InappOne97ServiceApi{

	 private static final Logger logger = Logger
				.getLogger(InappOne97ServiceApi.class.getName());
	 
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private IDaoService daoService;
	
	
	@Autowired
	private JMSService  jmsService;

	@PostConstruct
	public void init(){
		
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	
		
	}
	

	
	public boolean checkSubscriptionStatus(InappProcessRequest  inappProcessRequest, String msisdn,Integer id,String cgToken,InAppOne97Config one97InAppConfig
			
			){
	   
		InappOne97StatusCheck one97InappStatusCheck=null;
		try{
			one97InappStatusCheck=new InappOne97StatusCheck(true);
			one97InappStatusCheck.setCgToken(cgToken);
			one97InappStatusCheck.setRequestId(id);
		
			String url=InappOne97Constant
					.getUrl(one97InAppConfig.getCheckSubUrl(),""+one97InappStatusCheck.getCgToken()
					,msisdn
					,null,null,null,null);	
		
			one97InappStatusCheck.setStatusCheckUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		one97InappStatusCheck.setStatusCheckResp(httpResponse.getResponseCode()
				+" : "+httpResponse.getResponseStr());
		inappProcessRequest.setAdvertiserApiRequest(url);
		inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
		inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
		String arr[]=InappOne97Constant.parse(httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200&&arr[6].equalsIgnoreCase("ACTIVE")){
			one97InappStatusCheck.setSubStatus(arr[6]);
			return true;
		 }
		
		}catch(Exception ex){
			logger.error("statusCheck ",ex);
		
		}finally{
			jmsService.saveObject(one97InappStatusCheck);
		}		
		return false;
	
	}
	
	
public boolean checkChargeStatus(InappProcessRequest  inappProcessRequest,InAppOne97Config one97InAppConfig
		){
	   
	InappOne97StatusCheck one97InappStatusCheck=null;
	try{
		one97InappStatusCheck=new InappOne97StatusCheck(true);
		one97InappStatusCheck.setCgToken(inappProcessRequest.getCgToken());
		one97InappStatusCheck.setRequestId(inappProcessRequest.getId());
	   
		String url=InappOne97Constant
				.getUrl(one97InAppConfig.getCheckSubUrl(),""+one97InappStatusCheck.getCgToken()
				,inappProcessRequest.getMsisdn()
				,null,null,null,null);	
	
		one97InappStatusCheck.setStatusCheckUrl(url);
	HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
	one97InappStatusCheck.setStatusCheckResp(httpResponse.getResponseCode()
			+" : "+httpResponse.getResponseStr());
	inappProcessRequest.setAdvertiserApiRequest(url);
	inappProcessRequest.setAdvertiserApiResponseCode(httpResponse.getResponseCode());
	inappProcessRequest.setAdvertiserApiResponse(httpResponse.getResponseStr());
	String arr[]=InappOne97Constant.parse(httpResponse.getResponseStr());
	if(httpResponse.getResponseCode()==200&&arr[6].equalsIgnoreCase("ACTIVE")){
		one97InappStatusCheck.setSubStatus(arr[6]);
		return true;
	 }
	
	}catch(Exception ex){
		logger.error("statusCheck ",ex);
	
	}finally{
		jmsService.saveObject(one97InappStatusCheck);
	}		
	return false;
	}
}
