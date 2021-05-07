package net.mycomp.common.inapp;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.mycomp.common.service.IDaoService;
import net.persist.bean.AdnetworkOperatorConfig;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSInappProcessRequestListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSInappProcessRequestListener.class);

	@Autowired
	private IDaoService daoService;
	
   
	
	@Override
	public void onMessage(Message m) {
		
		InappProcessRequest inappProcessRequest=null;
		
		String msisdn=null;
		InappLiveReport inappLiveReport=null;
		try{
		ObjectMessage objectMessage = (ObjectMessage) m;
		inappProcessRequest=(InappProcessRequest)objectMessage.getObject();
		logger.info(msisdn+" onMessage::::: "+inappProcessRequest);
		inappProcessRequest.vwCampaignDetail = 
				MData.mapCamapignIdToVWCampaignDetail.get(inappProcessRequest.getCmpid());
		logger.info(msisdn+" onMessage::::: inappProcessRequest.vwserviceCampaignDetail :: "+inappProcessRequest
				.vwCampaignDetail );
		
		 inappLiveReport=new InappLiveReport(inappProcessRequest.vwCampaignDetail.getOpId(), 
				new Timestamp(System.currentTimeMillis()),inappProcessRequest.vwCampaignDetail.getCampaignId()
				,inappProcessRequest.getServiceId(),
				1);
		
		if(inappProcessRequest.getAction().equalsIgnoreCase(InappAction.SEND_PIN.action)){
			inappLiveReport.setPinRequestCount(inappProcessRequest.getPinRequestCount());
			if(inappProcessRequest.isSuccess()){
				inappLiveReport.setPinSendCount(1);
			}			
		}else if(inappProcessRequest.getAction().equalsIgnoreCase(InappAction.PIN_VALIDATION.action)){
			inappLiveReport.setPinValidationRequestCount(1);
			if(inappProcessRequest.isSuccess()){				
				inappLiveReport.setPinValidateCount(1);
				inappLiveReport.setPinValidateAmount(inappProcessRequest.getPinValidateAmount());
				inappLiveReport.setSendConversionCount(inappProcessRequest.getConversionSendToAdenetwork()?1:0);
				AdnetworkOperatorConfig adnetworkOperatorConfig = MData.mapAdnetworkOpConfig
						.get(inappProcessRequest.vwCampaignDetail.getOpId())
						.get(inappProcessRequest.vwCampaignDetail.getAdNetworkId());
				inappLiveReport.setSendConversionAmount(MUtility.
						toDouble(""+adnetworkOperatorConfig.getOpCpaValue(),0));
			}			
		}
		
			}catch(Exception ex){			
			logger.error(msisdn+" onMessage::::: "+inappProcessRequest+" error:  "+ex);
			
		}finally{
			try{
				daoService.generateInappLiveReport(inappLiveReport);
			}catch(Exception ex){
				logger.error(msisdn+" finally onMessage::::: "+inappProcessRequest,ex);
			}
			daoService.updateObject(inappProcessRequest);
		  }
		}
	}

