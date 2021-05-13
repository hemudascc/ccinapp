package net.mycomp.common.inapp;

import net.mycomp.publisher.AdwellyMediaPublisherService;
import net.mycomp.publisher.AudienceNestInappPublisherService;
import net.mycomp.publisher.CollectcentInappPublisherService;
import net.mycomp.publisher.ConversionMobiPublisherService;
import net.mycomp.publisher.DefaultInappPublisherService;
import net.mycomp.publisher.GlobocomInappPublisherService;
import net.mycomp.publisher.GulftechInappPublisherService;
import net.mycomp.publisher.HalawaPublisherServcie;
import net.mycomp.publisher.IInappPublisherService;
import net.mycomp.publisher.InnoveraSolutionsPublisherService;
import net.mycomp.publisher.MaclatoInappPublisherService;
import net.mycomp.publisher.MobilartsPublisherService;
import net.mycomp.publisher.MplusInappPublisherService;
import net.mycomp.publisher.OutrixwavePublisherService;
import net.mycomp.publisher.SaintInappPublisherService;
import net.mycomp.publisher.SarasMediaInappPublisherService;
import net.mycomp.publisher.SmashingLeadsInappPublisherService;
import net.mycomp.publisher.SmileDigitalInappPublisherService;
import net.mycomp.publisher.TOEInappPublisherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("inappOperatorService")
public class InappPublisherService implements IInappPublisherService{

	
	@Autowired
	private DefaultInappPublisherService defaultInappPublisherService;
	
	@Autowired
	@Qualifier("collectcentInappPublisherService")
	private CollectcentInappPublisherService collectcentInappPublisherService;
	
	@Autowired
	@Qualifier("globocomInappPublisherService")
	private GlobocomInappPublisherService globocomInappPublisherService;
	
	@Autowired
	@Qualifier("innoveraSolutionsPublisherService")
	private InnoveraSolutionsPublisherService innoveraSolutionsPublisherService;
	
	@Autowired
	@Qualifier("halawaPublisherServcie")
	private HalawaPublisherServcie halawaPublisherServcie;
	
	@Autowired
	@Qualifier("audienceNestInappPublisherService")
	private AudienceNestInappPublisherService audienceNestInappPublisherService;
	
	@Autowired
	@Qualifier("toeInappPublisherService")
	private TOEInappPublisherService toeInappPublisherService;
	
	@Autowired
	@Qualifier("mplusInappPublisherService")
	private MplusInappPublisherService mplusInappPublisherService;
	@Autowired
	@Qualifier("maclatoInappPublisherService")
	private MaclatoInappPublisherService maclatoInappPublisherService;
	
	@Autowired
	@Qualifier("mobilartsPublisherService")
	private MobilartsPublisherService mobilartsPublisherService;
	
	@Autowired
	@Qualifier("smileDigitalInappPublisherService")
	private SmileDigitalInappPublisherService smileDigitalInappPublisherService;
	
	@Autowired
	@Qualifier("saintInappPublisherService")
	private SaintInappPublisherService saintInappPublisherService;
	
	@Autowired
	@Qualifier("gulftechInappPublisherService")
	private GulftechInappPublisherService gulftechInappPublisherService;
	
	@Autowired
	@Qualifier("sarasMediaInappPublisherService")
	private SarasMediaInappPublisherService sarasMediaInappPublisherService;
	
	@Autowired
	@Qualifier("adwellyMediaPublisherService")
	private AdwellyMediaPublisherService adwellyMediaPublisherService;
	
	@Autowired
	@Qualifier("outrixwavePublisherService")  
	private OutrixwavePublisherService outrixwavePublisherService;
	
	@Autowired
	@Qualifier("conversionMobiPublisherService")
	private ConversionMobiPublisherService conversionMobiPublisherService;
	
	@Autowired
	@Qualifier("smashingLeadsInappPublisherService")
	private SmashingLeadsInappPublisherService smashingLeadsInappPublisherService;
	
private IInappPublisherService findProcessRequest(int adnetworkId){
	
	IInappPublisherService inappPublisherService=null;
		switch(adnetworkId){
		case InappConstant.INAPP_COLLECENT_ADNETWORK_ID:{
			inappPublisherService=collectcentInappPublisherService;
			break;
		}
		case InappConstant.INAPP_GLOBOCOM_ADNETWORK_ID:
		case InappConstant.INAPP_GLOBOCOM_DUPLICATE_ADNETWORK_ID:
		case InappConstant.INAPP_TRAFFIC_ADNETWORK_ID:
		case InappConstant.INAPP_XCELL_ADNETWORK_ID:{
			inappPublisherService=globocomInappPublisherService;
			break;
		}
		case InappConstant.INAPP_31_TOE_ADNETWORK_ID:{
			inappPublisherService=toeInappPublisherService;
			break;
		}
		
		case InappConstant.INAPP_AUDIENCE_NEST_ADNETWORK_ID:{
			inappPublisherService=audienceNestInappPublisherService;
			break;
		}
		case InappConstant.INAPP_MPLUS_ADNETWORK_ID:{
			inappPublisherService=mplusInappPublisherService;
			break;
		}
		case InappConstant.INAPP_MACLATO_ADNETWORK_ID:{
			inappPublisherService=maclatoInappPublisherService;
			break;
		}
		case InappConstant.INAPP_MOBILARTS_ADNETWORK_ID:{
			inappPublisherService=mobilartsPublisherService;
			break;
		}
		case InappConstant.INAPP_SMILE_DIGITAL_ADNETWORK_ID:{
			inappPublisherService=smileDigitalInappPublisherService;
			break;
		}
		case InappConstant.INAPP_SAINT_ADNETWORK_ID:{
			inappPublisherService=saintInappPublisherService;
			break;
		}
		case InappConstant.INAPP_INNOVERA_SOLUTIONS_ADNETWORK_ID:{
			inappPublisherService=innoveraSolutionsPublisherService;
			break;
		}
		case InappConstant.INAPP_HALAWA_ADNETWORK_ID:{
			inappPublisherService=halawaPublisherServcie;
			break;
		}
		case InappConstant.INAPP_GULFTECH_ADNETWORK_ID:{
			inappPublisherService=gulftechInappPublisherService;
			break;
		}
		case InappConstant.INAPP_SARAS_MEDIA_ADNETWORK_ID:{
			inappPublisherService=sarasMediaInappPublisherService;
			break;
		}case InappConstant.INAPP_ADWELLY_MEDIA_ADNETWORK_ID:{
			inappPublisherService=adwellyMediaPublisherService;
			break;
		}case InappConstant.INAPP_OUTRIX_WAVE_ADNETWORK_ID:{
			inappPublisherService=outrixwavePublisherService;
			break;
		}case InappConstant.INAPP_CONVERSION_MOBI_ADNETWORK_ID:{
			inappPublisherService=conversionMobiPublisherService;
			break;
		}case InappConstant.INAPP_SMASHING_LEADS_ADNETWORK_ID:{
			inappPublisherService=smashingLeadsInappPublisherService;
			break;
		}
		default:{
			inappPublisherService=defaultInappPublisherService;
			break;
		 }
		}
		return inappPublisherService;
	}

@Override
public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
	return findProcessRequest(inappProcessRequest.vwCampaignDetail.getAdNetworkId()).sendOtp(inappProcessRequest, modelAndView);
}

@Override
public boolean otpValidation(InappProcessRequest inappProcessRequest,
		ModelAndView modelAndView) {
	return findProcessRequest(inappProcessRequest.vwCampaignDetail.getAdNetworkId()).otpValidation(inappProcessRequest, modelAndView);
}

@Override
public boolean statusCheck(InappProcessRequest inappProcessRequest,
		ModelAndView modelAndView) {
	return findProcessRequest(inappProcessRequest.vwCampaignDetail.getAdNetworkId()).statusCheck(inappProcessRequest, modelAndView);
}

@Override
public String portalUrl(InappProcessRequest inappProcessRequest,
		ModelAndView modelAndView) {
	return findProcessRequest(inappProcessRequest.vwCampaignDetail.getAdNetworkId()).portalUrl(inappProcessRequest, modelAndView);
}

@Override
public boolean dct(InappProcessRequest inappProcessRequest) {
	return findProcessRequest(inappProcessRequest.vwCampaignDetail.getAdNetworkId()).dct(inappProcessRequest);
}
}
