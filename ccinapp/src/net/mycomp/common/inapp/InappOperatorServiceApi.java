package net.mycomp.common.inapp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.jpa.repository.JPASubscriberReg;
import net.mycomp.advertiser.vaca.VacaService;
import net.mycomp.ascenco.AscencoService;
import net.mycomp.common.inapp.collectcent.InappCollectcentService;
import net.mycomp.common.inapp.one97.InappOne97Service;
import net.mycomp.common.inapp.tmt.InappTmtService;
import net.mycomp.common.service.RedisCacheService;
import net.mycomp.inapp.adacts.AdactsService;
import net.mycomp.inapp.skmobi.SkmobiService;
import net.mycomp.kineticdigital.KinaticService;
import net.mycomp.shemaroo.ShemarooService;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWTrafficRouting;
import net.util.EnumReason;
import net.util.MConstantAdvertiser;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Service("inappOperatorServiceApi")
public class InappOperatorServiceApi extends  AbstractInappOperatorServiceApi{

	private static final Logger logger = Logger
			.getLogger(InappOperatorServiceApi.class.getName());
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	@Qualifier("inappTmtService")
	private InappTmtService inappTmtService;
	
	
	@Autowired
	@Qualifier("inappOne97Service")
	private InappOne97Service inappOne97Service;
	
	
	@Autowired
	@Qualifier("inappCollectcentService")
	private InappCollectcentService inappCollectcentService;
	
	@Autowired
	@Qualifier("skmobiService")
	private SkmobiService skmobiService;
	
	@Autowired
	private ShemarooService shemarooService;
	
	@Autowired
	private KinaticService kinaticService;
	
	@Autowired	
	private VacaService vacaService;
	
	@Autowired	
	private AdactsService adactsService;
	
	@Autowired
	@Qualifier("ascencoService")
	private AscencoService ascencoService;
	
    private IInappOperatorServiceApi findProcessRequest(int opId){
    	IInappOperatorServiceApi inappOperatorServiceApi=null;
    	switch(opId){
    	case MConstantAdvertiser.TMT:{
    		inappOperatorServiceApi=inappTmtService;
    		break;
    	}
    	case MConstantAdvertiser.ONE97:{
    		inappOperatorServiceApi=inappOne97Service;
    		break;
    	}
    	case MConstantAdvertiser.COLLECTCENT:{ 
    		inappOperatorServiceApi=inappCollectcentService;
    		break;
    	 }
    	case MConstantAdvertiser.SKMOBI:{
    		inappOperatorServiceApi=skmobiService;
    		break;
    	 }
    	case MConstantAdvertiser.SHEMAROO:{
    		inappOperatorServiceApi=shemarooService;
    		break;
    	 }
    	case MConstantAdvertiser.KINATIC:{
    		inappOperatorServiceApi=kinaticService;
    		break;
    	 }
    	case MConstantAdvertiser.ASCENCO:{
    		inappOperatorServiceApi=ascencoService;
    		break;
    	 }
    	case MConstantAdvertiser.VACA:{
    		inappOperatorServiceApi=vacaService;
    		break;
    	 }
    	case MConstantAdvertiser.ADACTS:{
    		inappOperatorServiceApi=adactsService;
    		break;
    	 }
    	}
		return inappOperatorServiceApi;
	}
    
    @Override
	public boolean sendPin(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		List<SubscriberReg> subscriberRegList=jpaSubscriberReg
				.findSubscriberRegByMsisdn(inappProcessRequest.getMsisdn());
		
		if(subscriberRegList!=null){
			for(SubscriberReg subscriberReg:subscriberRegList){
				if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
					inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);				
					return false;
				 }
			}			
		}
		
		List<VWTrafficRouting> vwServiceCampaignDetailList=MData.mapcampaignIdIdToVWTrafficRouting
				.get(inappProcessRequest.getCmpid());
		for(VWTrafficRouting vwTrafficRouting:vwServiceCampaignDetailList){
			if(vwTrafficRouting.isSendPin(subscriberRegList)){
			inappProcessRequest.setServiceId(vwTrafficRouting.getServiceId());
			break;
			}
		}
		
		if(inappProcessRequest.getServiceId()==null){
		for(VWTrafficRouting vwTrafficRouting:vwServiceCampaignDetailList){			
			vwTrafficRouting.getAtomicIntegerclickCounter().set(0);			
		   }
		}
		
		for(VWTrafficRouting vwTrafficRouting:vwServiceCampaignDetailList){
			if(vwTrafficRouting.isSendPin(subscriberRegList)){
			inappProcessRequest.setServiceId(vwTrafficRouting.getServiceId());
			break;
			}
		}		
		
		if(inappProcessRequest.getServiceId()!=null){
			redisCacheService.putObjectCacheValueByEvictionMinute(
					MConstants.INAPP_ROUTING_CAHCE_PREFIX+inappProcessRequest.getMsisdn()
					, inappProcessRequest.getServiceId(),60);  
		}
		
		net.persist.bean.Service service=MData
				.mapServiceIdToService.get(inappProcessRequest.getServiceId());
		findProcessRequest(service.getAdvertiserId())
		.statusCheck(inappProcessRequest, modelAndView);
		
		if(inappProcessRequest.getSuccess()){
			inappProcessRequest.setSuccess(false);//reset beacause set by status check
			inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);
			return false;
		}
		
		return findProcessRequest(service.getAdvertiserId())
				.sendPin(inappProcessRequest, modelAndView);
		
	}

	@Override
	public boolean validatePin(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
		
		inappProcessRequest.setServiceId(MUtility.toInt(Objects.toString(
				redisCacheService.getObjectCacheValue(MConstants.INAPP_ROUTING_CAHCE_PREFIX
						+inappProcessRequest.getMsisdn())),0));  
		
		net.persist.bean.Service service=MData.mapServiceIdToService
				.get(inappProcessRequest.getServiceId());
		SubscriberReg subscriberReg=jpaSubscriberReg
				.findSubscriberRegByMsisdnAndServiceId(inappProcessRequest.getMsisdn(),
						inappProcessRequest.getServiceId());
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);
			return false;
		}
		findProcessRequest(service.getAdvertiserId())
		.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.getSuccess()){
			inappProcessRequest.setSuccess(false);//reset beacause set by status check
			inappProcessRequest.setReason(EnumReason.ALREADY_SUBSCRIBED.reason);
			return false;
		}
		
		boolean status= findProcessRequest(service.getAdvertiserId())
				.validatePin(inappProcessRequest, modelAndView);
		if(inappProcessRequest.getSuccess()){	
			 subscriberReg=new SubscriberReg();
			subscriberReg.setMsisdn(inappProcessRequest.getMsisdn());
			subscriberReg.setServiceId(inappProcessRequest.getServiceId());
			subscriberReg.setStatus(MConstants.SUBSCRIBED);
			subscriberReg.setStatusDescp(MConstants.SUBSCRIBED_DESC);
			subscriberReg.setOperatorId(service.getOpId());
			subscriberReg.setRegData(new Timestamp(System.currentTimeMillis()));
			jpaSubscriberReg.save(subscriberReg);
			redisCacheService.putObjectCacheValueByEvictionMinute(MConstants.INAPP_ROUTING_BLOCK_CAHCE_PREFIX
					+inappProcessRequest.getMsisdn()+inappProcessRequest.getServiceId(), true, 6);
		}
		return status;
	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		inappProcessRequest.setServiceId(MUtility.toInt(Objects.toString(redisCacheService.getObjectCacheValue(
				MConstants.INAPP_ROUTING_CAHCE_PREFIX+inappProcessRequest.getMsisdn())),0));  
		net.persist.bean.Service service=MData.mapServiceIdToService.get(inappProcessRequest.getServiceId());
		return findProcessRequest(service.getAdvertiserId())
				.statusCheck(inappProcessRequest, modelAndView);

	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		try{
		inappProcessRequest.setServiceId(MUtility.toInt(Objects.toString(redisCacheService.getObjectCacheValue(
				MConstants.INAPP_ROUTING_CAHCE_PREFIX+inappProcessRequest.getMsisdn())),0)); 	
		if(inappProcessRequest.getServiceId()!=null||inappProcessRequest.getServiceId()==0){
			List<SubscriberReg> listSubscriberReg=
					jpaSubscriberReg.findSubscriberRegByMsisdn(inappProcessRequest.getMsisdn());
		 for(SubscriberReg subscriberReg:listSubscriberReg){
			 inappProcessRequest.setServiceId(subscriberReg.getServiceId());
			 if(subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				 break;
			 }
		 }
		}
		net.persist.bean.Service service=MData.mapServiceIdToService.get(inappProcessRequest.getServiceId());
		return findProcessRequest(service.getAdvertiserId())
				.portalUrl(inappProcessRequest, modelAndView);
	 }catch(Exception ex){
		 logger.error("Exception ",ex);
	 }
	 return null;	
	}
	
	@Override
	public boolean dct(InappProcessRequest inappProcessRequest) {
		
		inappProcessRequest.setServiceId(MUtility.toInt(Objects.toString(redisCacheService.getObjectCacheValue(
				MConstants.INAPP_ROUTING_CAHCE_PREFIX+inappProcessRequest.getMsisdn())),0)); 
		
		net.persist.bean.Service service=MData.mapServiceIdToService.get(inappProcessRequest.getServiceId());
		return findProcessRequest(service.getAdvertiserId())
				.dct(inappProcessRequest);
	}

}
