package net.mycomp.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import net.mycomp.advertiser.vaca.VacaConfig;
import net.mycomp.ascenco.AscencoConfig;
import net.mycomp.common.inapp.InappAutomatedProcessRequest;
import net.mycomp.common.inapp.collectcent.CollectcentServiceConfig;
import net.mycomp.common.inapp.one97.InAppOne97Config;
import net.mycomp.common.inapp.tmt.InAppTmtConfig;
import net.mycomp.inapp.adacts.AdactsConfig;
import net.mycomp.inapp.apalya.ApalyaConfig;
import net.mycomp.inapp.audiencenest.AudiencenestConfig;
import net.mycomp.inapp.raone.RaoneConfig;
import net.mycomp.inapp.skmobi.SkmobiConfig;
import net.mycomp.kineticdigital.KinaticConfig;
import net.mycomp.shemaroo.ShemarooConfig;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Aggregator;
import net.persist.bean.CampaignDetails;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.TrafficRouting;
import net.util.JsonMapper;
import net.util.MConstantAdvertiser;

@Service("automatedServiceApi")
public class AutomatedServiceApi implements AutomatedService{

	private static final Logger logger = Logger.getLogger(AutomatedServiceApi.class);
	
	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager entityManager;
	
	@Autowired
	private IDaoService daoService;
	
	String queryStr = "";
	Query query = null;
	String table = "";

	public InappAutomatedProcessRequest findAdvertiserConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		InappAutomatedProcessRequest inprocessConfig = null;
		switch (inappAutomatedProcessRequest.getAdvertiserId()) {
		case MConstantAdvertiser.TMT: {
			table = "tb_inapp_tmt_config";
			inprocessConfig = new InAppTmtConfig();
			break;
		}
		case MConstantAdvertiser.ONE97: {
			table = "tb_inapp_one97_config";
			inprocessConfig = new InAppOne97Config();
			break;
		}
		case MConstantAdvertiser.COLLECTCENT: {
			table = "tb_inapp_collectcent_service_config";
			inprocessConfig = new CollectcentServiceConfig();
			break;
		}
		case MConstantAdvertiser.SKMOBI: {
			table = "tb_skmobi_config";
			inprocessConfig = new SkmobiConfig();
			break;
		}
		case MConstantAdvertiser.SHEMAROO: {
			table = "tb_shemaroo_config";
			inprocessConfig = new ShemarooConfig();
			break;
		}
		case MConstantAdvertiser.KINATIC: {
			table = "tb_kinatic_config";
			inprocessConfig = new KinaticConfig();
			break;
		}
		case MConstantAdvertiser.ASCENCO: {
			table = "tb_ascenco_config";
			inprocessConfig = new AscencoConfig();
			break;
		}
		case MConstantAdvertiser.VACA: {
			table = "tb_vaca_config";
			inprocessConfig = new VacaConfig();
			break;
		}
		case MConstantAdvertiser.ADACTS: {
			table = "tb_adacts_config";
			inprocessConfig = new AdactsConfig();
			break;
		}
		case MConstantAdvertiser.APALYA: {
			table = "tb_apalya_config";
			inprocessConfig = new ApalyaConfig();
			break;
		}
		case MConstantAdvertiser.RAONE: {
			table = "tb_raone_config";
			inprocessConfig = new RaoneConfig();
			break;
		}
		case MConstantAdvertiser.AUDIENCENEST: {
			table = "tb_audiencenest_config";
			inprocessConfig = new AudiencenestConfig();
			break;
		}
		}
		return inprocessConfig;

	}

	@Override
	public Object advertiserConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest,
			ModelAndView modelAndView) {
    		InappAutomatedProcessRequest inprocessConfig = findAdvertiserConfig(inappAutomatedProcessRequest);
			Map<String, String> responseMap = new HashMap<String, String>();
			responseMap.put("statusCode", "500");
			responseMap.put("message", "InAppRaoneConfig not added");
			responseMap.put("status", "Fail");
			inprocessConfig.setId(inappAutomatedProcessRequest.getId());
			inprocessConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
			inprocessConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
			inprocessConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
			inprocessConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
			inprocessConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
			inprocessConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
			inprocessConfig.setStatus(inappAutomatedProcessRequest.getStatus());
			inprocessConfig.setAmount(inappAutomatedProcessRequest.getAmount());
			inprocessConfig.setAuthorization(inappAutomatedProcessRequest.getAuthorization());
			inprocessConfig.setCampId(inappAutomatedProcessRequest.getCampId());
			inprocessConfig.setCheckSubUrl(inappAutomatedProcessRequest.getCheckSubUrl());
			inprocessConfig.setDctUrl(inappAutomatedProcessRequest.getDctUrl());
			inprocessConfig.setOperatorDetail(inappAutomatedProcessRequest.getOperatorDetail());
			inprocessConfig.setPinSendUrl(inappAutomatedProcessRequest.getPinSendUrl());
			inprocessConfig.setPinValidationUrl(inappAutomatedProcessRequest.getPinValidationUrl());
			inprocessConfig.setPortalUrl2(inappAutomatedProcessRequest.getPortalUrl2());
			inprocessConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
			inprocessConfig.setProductId(inappAutomatedProcessRequest.getProductId());
			inprocessConfig.setResendPinUrl(inappAutomatedProcessRequest.getResendPinUrl());
			inprocessConfig.setValidity(inappAutomatedProcessRequest.getValidity());
			logger.info("inprocessObject: "+inprocessConfig.getClass().getName());
				try {
					queryStr = "select e.* from "+table+" e where e.service_id = :serviceId";
					logger.info("quer: "+queryStr);
					query = entityManager.createNativeQuery(queryStr, inprocessConfig.getClass());
					query.setParameter("serviceId", inappAutomatedProcessRequest.getServiceId());
					Boolean isAlreadyExist = daoService.checkExistingRecord(query);
					logger.info(" isAlreadyExist : "+isAlreadyExist);
					if(isAlreadyExist) {  
						responseMap.put("statusCode", "200");  
						responseMap.put("message", inprocessConfig.getClass().getName()+" already exist");
						responseMap.put("status", "Success");
					}else {
						daoService.saveObject(inprocessConfig);
						responseMap.put("statusCode", "200");
						responseMap.put("message", inprocessConfig.getClass().getName()+" added successfully");
						responseMap.put("status", "Success");
					}
				} catch (Exception e) {
					logger.error("ex: " + e);
					responseMap.put("statusCode", "500");
					responseMap.put("message", inprocessConfig.getClass().getName()+" error"+e);
					responseMap.put("status", "Fail");
					}
			return JsonMapper.getObjectToJson(responseMap);
}

	/* In App Automated API methods */

	@Override
	public Object findAllCountry() {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Object> countryList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Country Not Found");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_country e";
			query = entityManager.createNativeQuery(queryStr, Country.class);
			countryList = daoService.getDataList(query);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Country List");
			responseMap.put("status", "Success");
			responseMap.put("countryList", JsonMapper.getObjectToJson(countryList));
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);
	}

	@Override
	public Object findAllProduct() {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Object> productList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Product Not Found");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_product e";
			query = entityManager.createNativeQuery(queryStr, Product.class);
			productList = daoService.getDataList(query);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Product List");
			responseMap.put("status", "Success");
			responseMap.put("productList", "" + JsonMapper.getObjectToJson(productList));
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object findAllAggregator() {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Object> aggregatorList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Aggregator not found");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_aggregator e";
			query = entityManager.createNativeQuery(queryStr, Aggregator.class);
			aggregatorList = daoService.getDataList(query);
			responseMap.put("statusCode", "500");
			responseMap.put("message", "Aggregator List");
			responseMap.put("status", "Fail");
			responseMap.put("aggregatorList", "" + JsonMapper.getObjectToJson(aggregatorList));
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object findAllOperator() {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Object> operatorList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "operator not found");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_operators e";
			query = entityManager.createNativeQuery(queryStr, Operator.class);
			operatorList= daoService.getDataList(query);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "operatorList List");
			responseMap.put("status", "Success");
			responseMap.put("operatorList", "" + JsonMapper.getObjectToJson(operatorList));
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);
	}
	
	@Override
	public Object findAllSerivce() {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Object> serivceList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Service not found");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_service e";
			query = entityManager.createNativeQuery(queryStr, net.persist.bean.Service.class);
			serivceList = daoService.getDataList(query);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Service List");
			responseMap.put("status", "Success");
			responseMap.put("serivceList", "" + JsonMapper.getObjectToJson(serivceList));
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object findAllCampaignDetails() {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Object> campaignDetails = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "CampaignDetails not fond");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_campaign_details e";
			query = entityManager.createNativeQuery(queryStr, CampaignDetails.class);
			campaignDetails = daoService.getDataList(query);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "CampaignDetails List");
			responseMap.put("status", "Success");
			responseMap.put("campaignDetails", "" + JsonMapper.getObjectToJson(campaignDetails));
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveCampaignDetails(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Campaign Details not added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_campaign_details e where e.campaign_name= :campaignName and e.ad_network_id = :adNetworkId and e.op_id = :opId";
			query = entityManager.createNativeQuery(queryStr, CampaignDetails.class);
			query.setParameter("campaignName", inappAutomatedProcessRequest.getCampaignDetails().getCampaignName());
			query.setParameter("adNetworkId", inappAutomatedProcessRequest.getCampaignDetails().getAdNetworkId());
			query.setParameter("opId", inappAutomatedProcessRequest.getCampaignDetails().getOpId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist CampaignDetails : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Campaign Details Already Exist");
				responseMap.put("status", "Success");
			} else {
				daoService.saveObject(inappAutomatedProcessRequest.getCampaignDetails());
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Campaign Details added Successfully");
				responseMap.put("status", "Success");
			}
		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveCountry(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Country not added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_country e where e.name= :name";
			query = entityManager.createNativeQuery(queryStr, Country.class);
			query.setParameter("name", inappAutomatedProcessRequest.getCountry().getName());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist Country : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Country Already Exist");
				responseMap.put("status", "Success");
			} else {

				daoService.saveObject(inappAutomatedProcessRequest.getCountry());
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Country Added Successfully");
				responseMap.put("status", "Success");
			}
		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override 
	public Object saveProduct(InappAutomatedProcessRequest inappAutomatedProcessRequest) {

		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Product not added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_product e where e.product_name= :productName";
			query = entityManager.createNativeQuery(queryStr, Product.class);
			logger.info("productName: "+inappAutomatedProcessRequest.getProduct().getProductName());
			query.setParameter("productName", inappAutomatedProcessRequest.getProduct().getProductName());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist Product : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Product Already Exist");
				responseMap.put("status", "Success");
			} else {
				daoService.saveObject(inappAutomatedProcessRequest.getProduct());
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Product Added Successfully");
				responseMap.put("status", "Success");
			}
		}  catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);
	}

	@Override
	public Object saveSerivce(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Service not added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_service e where e.advertiser_id= :advertiserId and e.service_name = :serviceName and e.op_id = :opId";
			query = entityManager.createNativeQuery(queryStr, net.persist.bean.Service.class);
			query.setParameter("advertiserId", inappAutomatedProcessRequest.getService().getAdvertiserId());
			query.setParameter("serviceName", inappAutomatedProcessRequest.getService().getServiceName());
			query.setParameter("opId", inappAutomatedProcessRequest.getService().getOpId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist Service : "+isAlreadyExist);
			if (isAlreadyExist) {	responseMap.put("statusCode", "200");
				responseMap.put("message", "Service Already Exist");
				responseMap.put("status", "Success");
			} else {
				daoService.saveObject(inappAutomatedProcessRequest.getService());
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Service Added Successfully");
				responseMap.put("status", "Success");
			}
		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveOperator(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Operator not added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_operators e where e.operator_name= :operatorName and e.aggregator_id = :aggregatorId and e.country_id = :countryId";
			query = entityManager.createNativeQuery(queryStr, Operator.class);
			query.setParameter("operatorName", inappAutomatedProcessRequest.getOperator().getOperatorName());
			query.setParameter("aggregatorId", inappAutomatedProcessRequest.getOperator().getAggregatorId());
			query.setParameter("countryId", inappAutomatedProcessRequest.getOperator().getCountryId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist Operator : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Operator Already Exist");
				responseMap.put("status", "Success");
			} else {
				daoService.saveObject(inappAutomatedProcessRequest.getOperator());
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Operator Added Successfully");
				responseMap.put("status", "Success");
			}

		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveAdnetworkOperatorConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "AdNetwork Operator Config Not Added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_adnetwork_operator_config e where e.ad_network_id= :adNetworkId and e.operator_id = :operatorId";
			query = entityManager.createNativeQuery(queryStr, AdnetworkOperatorConfig.class);
			query.setParameter("adNetworkId", inappAutomatedProcessRequest.getAdnetworkOperatorConfig().getAdNetworkId());
			query.setParameter("operatorId", inappAutomatedProcessRequest.getAdnetworkOperatorConfig().getOperatorId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist AdnetworkOperatorConfig : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "AdNetwork Operator Config Already Exist");
				responseMap.put("status", "Success");
			} else {
				daoService.saveObject(inappAutomatedProcessRequest.getAdnetworkOperatorConfig());
				responseMap.put("statusCode", "200");
				responseMap.put("message", "AdNetwork Operator Config Added Successfully");
				responseMap.put("status", "Success");
			}

		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveTrafficRouting(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "TrafficRouting not added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from traffic_routing e where e.campaign_id= :campaignId and e.service_id = :serviceId";
			query = entityManager.createNativeQuery(queryStr, TrafficRouting.class);
			query.setParameter("campaignId", inappAutomatedProcessRequest.getTrafficRouting().getCampaignId());
			query.setParameter("serviceId", inappAutomatedProcessRequest.getTrafficRouting().getServiceId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist TrafficRouting : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "TrafficRouting Already Exist");
				responseMap.put("status", "Success");
			}else {
				daoService.saveObject(inappAutomatedProcessRequest.getTrafficRouting());
				responseMap.put("statusCode", "200");
				responseMap.put("message", "TrafficRouting added Successfully");
				responseMap.put("status", "Success");
			}  
		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
		}

		return JsonMapper.getObjectToJson(responseMap);
	}



	
}
