package net.mycomp.common.inapp.tmt;

import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import net.common.jms.JMSService;
import net.jpa.repository.JPAInappTmtConfig;
import net.mycomp.common.service.IDaoService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("inappTmtServiceApi")
public class InappTmtServiceApi{

	 private static final Logger logger = Logger
				.getLogger(InappTmtServiceApi.class.getName());
	 
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAInappTmtConfig  jpaTmtInappConfig;
	
	@Autowired
	private JMSService  jmsService;
	
	@Value("${jdbc.db.name}")
	private String dbName;
	 
	@PostConstruct
	public void init(){
		
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	
		
	}
	

	
	public boolean checkSubscriptionStatus(String msisdn,Integer id,String cgToken,InAppTmtConfig tmtInAppConfig
			
			){
	   
		InappTmtStatusCheck tmtInappStatusCheck=null;
		try{
			tmtInappStatusCheck=new InappTmtStatusCheck(true);
			tmtInappStatusCheck.setCgToken(cgToken);
			tmtInappStatusCheck.setRequestId(id);
		
		String url=InappTmtConstant.getUrl(tmtInAppConfig.getCheckSubUrl(),
				null ,msisdn
				, tmtInAppConfig, "", "");
		
		tmtInappStatusCheck.setStatusCheckUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		tmtInappStatusCheck.setStatusCheckResp(httpResponse.getResponseCode()
				+" : "+httpResponse.getResponseStr());
		// {"status":"0","description":"active","prodid":"Have Fun Games","sc":"",
		//"keyword":"","trxId":1025652606,"chargeStatus":false}
		
		if(httpResponse.getResponseCode()==200){
			
			Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			tmtInappStatusCheck.setSubStatus(Objects.toString(map.get("status")));
			tmtInappStatusCheck.setDescription(Objects.toString(map.get("description")));
			tmtInappStatusCheck.setTrxId(Objects.toString(map.get("trxId")));
			tmtInappStatusCheck.setChargeStatus(Objects.toString(map.get("chargeStatus")));
			tmtInappStatusCheck.setSubStatus(Objects.toString(map.get("status")));			
			 if(map!=null&&Objects.toString(map.get("status")).equals("0")&&Objects.toString(
					 map.get("description")).equals("active")){						
				 return true;
			}
		 }
		
		}catch(Exception ex){
			logger.error("statusCheck ",ex);
		
		}finally{
			jmsService.saveObject(tmtInappStatusCheck);
		}		
		return false;
	}
	
	
public boolean checkChargeStatus(String msisdn,Integer id,String cgToken,InAppTmtConfig tmtInAppConfig,ModelAndView modelAndView){
	   
		InappTmtStatusCheck tmtInappStatusCheck=null;
		try{
			tmtInappStatusCheck=new InappTmtStatusCheck(true);
			tmtInappStatusCheck.setCgToken(cgToken);
			tmtInappStatusCheck.setRequestId(id);
		
		String url=InappTmtConstant.getUrl(tmtInAppConfig.getCheckSubUrl(),
				null ,msisdn
				, tmtInAppConfig, "", "");
		
		tmtInappStatusCheck.setStatusCheckUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		tmtInappStatusCheck.setStatusCheckResp(httpResponse.getResponseCode()
				+" : "+httpResponse.getResponseStr());
		// {"status":"0","description":"active","prodid":"Have Fun Games","sc":"",
		//"keyword":"","trxId":1025652606,"chargeStatus":false}
		
		if(httpResponse.getResponseCode()==200){
			
			Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			tmtInappStatusCheck.setSubStatus(Objects.toString(map.get("status")));
			tmtInappStatusCheck.setDescription(Objects.toString(map.get("description")));
			tmtInappStatusCheck.setTrxId(Objects.toString(map.get("trxId")));
			tmtInappStatusCheck.setChargeStatus(Objects.toString(map.get("chargeStatus")));
			tmtInappStatusCheck.setSubStatus(Objects.toString(map.get("status")));
			
			 if(map!=null&&Objects.toString(map.get("status")).equals("0")&&Objects.toString(
					 map.get("description")).equals("active")
					 &&tmtInappStatusCheck.getChargeStatus().equalsIgnoreCase("true")){						
				 return true;
			}
		 }
		
		}catch(Exception ex){
			logger.error("statusCheck ",ex);
		
		}finally{
			jmsService.saveObject(tmtInappStatusCheck);
		}		
		return false;
	}

}
