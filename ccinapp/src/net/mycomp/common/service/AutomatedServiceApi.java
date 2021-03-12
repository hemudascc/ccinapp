package net.mycomp.common.service;

import java.sql.Timestamp;
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
import net.persist.bean.Adnetworks;
import net.persist.bean.Advertiser;
import net.persist.bean.Aggregator;
import net.persist.bean.CampaignDetails;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.ServiceConfigTrans;
import net.persist.bean.TrafficRouting;
import net.util.JsonMapper;
import net.util.MConstantAdvertiser;
import net.util.MData;
import net.util.MUtility;

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
	@Transactional
	public Object advertiserConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest,
			ModelAndView modelAndView) {
		InappAutomatedProcessRequest inprocessConfig = null;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "Service not added");
		responseMap.put("status", false);
		boolean isAdvertiserAdded = false;
				try {
//					List<Product> product MData.mapIdToProduct.get(inappAutomatedProcessRequest.getProductId());
					
					queryStr = "select e.* from tb_operators e where e.operator_id= :operatorId";
					query = entityManager.createNativeQuery(queryStr, Operator.class);
					query.setParameter("operatorId", inappAutomatedProcessRequest.getOperatorId());
					Operator operator = (Operator)daoService.getSingleRecord(query);
					logger.info("operator: "+operator);
					inappAutomatedProcessRequest.setOperatorDetail(operator.getOperatorName());
					inappAutomatedProcessRequest.setOperatorName(operator.getOperatorName());
					queryStr = "select e.* from tb_product e where e.id=:id";
					query = entityManager.createNativeQuery(queryStr, Product.class);
					query.setParameter("id", inappAutomatedProcessRequest.getProductId());
					Product product = (Product)daoService.getSingleRecord(query);	  
					logger.info("product: "+product);
					Advertiser adveriser = MData.mapIdToAdvertiser.get(inappAutomatedProcessRequest.getAdvertiserId());
					net.persist.bean.Service service = new net.persist.bean.Service();
//					service.setServiceId(MUtility.toInt(request.getParameter("serviceid"), 0));
					service.setServiceName(adveriser.getAdvertiserName()+"_"+inappAutomatedProcessRequest.getOperatorName()+"_"+product.getProductName());
					service.setServiceDesc(adveriser.getAdvertiserName()+"_"+inappAutomatedProcessRequest.getOperatorName()+"_"+product.getProductName());
					service.setOpId(inappAutomatedProcessRequest.getOperatorId());
					service.setAdvertiserId(inappAutomatedProcessRequest.getAdvertiserId());
					service.setProductId(inappAutomatedProcessRequest.getProductId()); 
					service.setOtpLength(inappAutomatedProcessRequest.getOtpLength());
//					service.setValidity(0);
//					service.setPricePoint(MUtility.toInt(request.getParameter("pricepoint"), 0));
					service.setStatus(true);			
					inappAutomatedProcessRequest.setService(service);	
					boolean isServiceAdded = saveSerivce(inappAutomatedProcessRequest);
					if(isServiceAdded) {
					inappAutomatedProcessRequest.setRequestStatus("Data added in Service Table");
					inappAutomatedProcessRequest.setServiceId(service.getServiceId());	
		    		inprocessConfig = findAdvertiserConfig(inappAutomatedProcessRequest);
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
					inprocessConfig.setOperatorDetail(inappAutomatedProcessRequest.getOperatorName());
					inprocessConfig.setPinSendUrl(inappAutomatedProcessRequest.getPinSendUrl());
					inprocessConfig.setPinValidationUrl(inappAutomatedProcessRequest.getPinValidationUrl());
					inprocessConfig.setPortalUrl2(inappAutomatedProcessRequest.getPortalUrl2());
					inprocessConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
					inprocessConfig.setProductId(inappAutomatedProcessRequest.getProductId());
					inprocessConfig.setResendPinUrl(inappAutomatedProcessRequest.getResendPinUrl());
					inprocessConfig.setValidity(inappAutomatedProcessRequest.getValidity());
					logger.info("inprocessObject: "+inprocessConfig.getClass().getSimpleName());
					queryStr = "select e.* from "+table+" e where e.service_id = :serviceId";
					logger.info("quer: "+queryStr);
					query = entityManager.createNativeQuery(queryStr, inprocessConfig.getClass());
					query.setParameter("serviceId", inappAutomatedProcessRequest.getServiceId());
					Boolean isAlreadyExist = daoService.checkExistingRecord(query);
					logger.info(" isAlreadyExist : "+isAlreadyExist);
					if(isAlreadyExist) {  
						inappAutomatedProcessRequest.setRequestStatus(inprocessConfig.getClass().getSimpleName()+" already exist");
						responseMap.put("message", inprocessConfig.getClass().getSimpleName()+" already exist");
						responseMap.put("status", false);
					}else {

						isAdvertiserAdded = daoService.saveObject(inprocessConfig);
						if(isAdvertiserAdded) {
							inappAutomatedProcessRequest.setRequestStatus(inprocessConfig.getClass().getSimpleName()+" added successfully");
						}else {
							inappAutomatedProcessRequest.setRequestStatus("Not added in "+inprocessConfig.getClass().getSimpleName());
						}
						responseMap.put("message", inprocessConfig.getClass().getSimpleName()+" added successfully");
						responseMap.put("status", true);
					}
					saveServiceConfigTrans(inappAutomatedProcessRequest);
					}else {
						inappAutomatedProcessRequest.setRequestStatus("Service already exist");
						saveServiceConfigTrans(inappAutomatedProcessRequest);
					}
	
					  
				} catch (Exception e) {
					logger.error("ex: " + e);  
					responseMap.put("message", inprocessConfig.getClass().getSimpleName()+" error"+e);
					responseMap.put("status", false);
					}
			return JsonMapper.getObjectToJson(responseMap);
}

	/* In App Automated API methods */

	@Override
	public Object findAllCountry() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Object> countryList = null;
		responseMap.put("message", "Country Not Found");
		responseMap.put("status", false);
		try {
			queryStr = "select e.* from tb_country e";
			query = entityManager.createNativeQuery(queryStr, Country.class);
			countryList = daoService.getDataList(query);
			if(countryList.size()>0) {
			responseMap.put("message", "Country List");
			responseMap.put("status", true);
			responseMap.put("countryList", countryList);
			}
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("status", false);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);
	}

	@Override
	public Object findAllProduct() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Object> productList = null;
		responseMap.put("message", "Product Not Found");
		responseMap.put("status", false);
		try {
			queryStr = "select e.* from tb_product e";
			query = entityManager.createNativeQuery(queryStr, Product.class);
			productList = daoService.getDataList(query);
			if(productList.size()>0) {
			responseMap.put("message", "Product List");
			responseMap.put("status", false);
			responseMap.put("productList", productList);
			}
		} catch (Exception e) { 
			logger.error("ex: " + e);  
			responseMap.put("status", false);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object findAllAggregator() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Object> aggregatorList = null;
		responseMap.put("message", "Aggregator not found");
		responseMap.put("status", false);
		try {
			queryStr = "select e.* from tb_aggregator e";
			query = entityManager.createNativeQuery(queryStr, Aggregator.class);
			aggregatorList = daoService.getDataList(query);
			if(aggregatorList.size()>0) {
			responseMap.put("message", "Aggregator List");
			responseMap.put("status", true);
			responseMap.put("aggregatorList", aggregatorList);
			}
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("status", false);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object findAllOperator() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Object> operatorList = null;
		responseMap.put("message", "operator not found");
		responseMap.put("status", false);
		try {
			queryStr = "select e.* from tb_operators e";
			query = entityManager.createNativeQuery(queryStr, Operator.class);
			operatorList= daoService.getDataList(query);
			if(operatorList.size()>0) {
			responseMap.put("message", "Operator List");
			responseMap.put("status", true);
			responseMap.put("operatorList",operatorList);
			}
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			responseMap.put("status", false);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);
	}
	
	@Override
	public Object findAllSerivce() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Object> serivceList = null;
		responseMap.put("message", "Service not found");
		responseMap.put("status", false);
		try {
			queryStr = "select e.* from tb_service e";
			query = entityManager.createNativeQuery(queryStr, net.persist.bean.Service.class);
			serivceList = daoService.getDataList(query);
			if(serivceList.size()>0) {
			responseMap.put("message", "Service List");
			responseMap.put("status", true);
			responseMap.put("serivceList",serivceList);
			}
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			responseMap.put("status", false);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object findAllCampaignDetails() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Object> campaignDetails = null;
		responseMap.put("message", "Campaign not fond");
		responseMap.put("status", false);
		try {
			queryStr = "select e.* from tb_campaign_details e";
			query = entityManager.createNativeQuery(queryStr, CampaignDetails.class);
			campaignDetails = daoService.getDataList(query);
			if(campaignDetails.size()>0) {
			responseMap.put("message", "Campaign List");  
			responseMap.put("status", true);
			responseMap.put("campaignList", campaignDetails);
			}
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			responseMap.put("status", false);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	@Transactional
	public Object saveCampaignDetails(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "Campaign Details not added");
		responseMap.put("status", false);
		try {
			queryStr = "select e.* from tb_campaign_details e where e.campaign_name= :campaignName and e.ad_network_id = :adNetworkId and e.op_id = :opId";
			query = entityManager.createNativeQuery(queryStr, CampaignDetails.class);
			query.setParameter("campaignName", inappAutomatedProcessRequest.getCampaignDetails().getCampaignName());
			query.setParameter("adNetworkId", inappAutomatedProcessRequest.getCampaignDetails().getAdNetworkId());
			query.setParameter("opId", inappAutomatedProcessRequest.getCampaignDetails().getOpId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist CampaignDetails : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("message", "Campaign Details Already Exist");
				responseMap.put("status", false);
			} else {
				boolean isCampaignAdded = daoService.saveObject(inappAutomatedProcessRequest.getCampaignDetails());
				if(isCampaignAdded) {
					logger.info("generated Key: "+inappAutomatedProcessRequest.getCampaignDetails().getCampaignId());
					TrafficRouting trafficRouting = new TrafficRouting();
					trafficRouting.setCampaignId(inappAutomatedProcessRequest.getCampaignDetails().getCampaignId());
					trafficRouting.setServiceId(inappAutomatedProcessRequest.getServiceId());
					trafficRouting.setPercentageOfTraffic(0);
					trafficRouting.setTrafiicRoutingStatus(true);
					inappAutomatedProcessRequest.setTrafficRouting(trafficRouting);
					
					boolean isTrafficRoutingAdded = saveTrafficRouting(inappAutomatedProcessRequest);
					if(isTrafficRoutingAdded) {
						AdnetworkOperatorConfig adnetworkOperatorConfig = new AdnetworkOperatorConfig();
						adnetworkOperatorConfig.setAdNetworkId(inappAutomatedProcessRequest.getCampaignDetails().getAdNetworkId());
						adnetworkOperatorConfig.setOperatorId(inappAutomatedProcessRequest.getCampaignDetails().getOpId());
						adnetworkOperatorConfig.setSkipNumber(0);
						adnetworkOperatorConfig.setStatus(true);
						adnetworkOperatorConfig.setOpCpaValue(0.0);
						adnetworkOperatorConfig.setDuplicateBlockStatus(false);
						adnetworkOperatorConfig.setAdBlockStatus(false);
						inappAutomatedProcessRequest.setAdnetworkOperatorConfig(adnetworkOperatorConfig);
						saveAdnetworkOperatorConfig(inappAutomatedProcessRequest);
						responseMap.put("message", "Campaign Details added Successfully");
						responseMap.put("status", true);
//						if(adNetworkOpConfig){
//							responseMap.put("message", "Campaign Details added Successfully");
//							responseMap.put("status", true);
//						}else {
//							responseMap.put("message", "Error in adding adnetwork operator config");
//							responseMap.put("status", false);
//						}
					}else {
						responseMap.put("message", "Error in adding traffic routing");
						responseMap.put("status", false);
					}
				}else {
					responseMap.put("message", "Error in adding campaign");
					responseMap.put("status", false);
				}
				
			}
		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			responseMap.put("status", false);
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

		Map<String, Object> responseMap = new HashMap<String, Object>();
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
				responseMap.put("message", "Product Already Exist");  
				responseMap.put("status", false);
			} else {
				daoService.saveObject(inappAutomatedProcessRequest.getProduct());
				responseMap.put("message", "Product Added Successfully");  
				responseMap.put("status", true);
			}
		}  catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("status", false);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);
	}


	public boolean saveSerivce(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		try {
			queryStr = "select e.* from tb_service e where e.advertiser_id= :advertiserId and e.service_name = :serviceName and e.op_id = :opId";
			query = entityManager.createNativeQuery(queryStr, net.persist.bean.Service.class);
			query.setParameter("advertiserId", inappAutomatedProcessRequest.getService().getAdvertiserId());
			query.setParameter("serviceName", inappAutomatedProcessRequest.getService().getServiceName());
			query.setParameter("opId", inappAutomatedProcessRequest.getService().getOpId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist Service : "+isAlreadyExist);
			return (isAlreadyExist)?false: daoService.saveObject(inappAutomatedProcessRequest.getService());
//			{	
//			} else {
//				daoService.saveObject(inappAutomatedProcessRequest.getService());
//			}
		}catch (Exception e) {
			logger.error("ex: " + e);
		}
		return false;

	}

	@Override
	public Object saveOperator(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "Operator not added");
		responseMap.put("status", "Fail");
		try {
			queryStr = "select e.* from tb_operators e where e.operator_name= :operatorName";
			query = entityManager.createNativeQuery(queryStr, Operator.class);
			query.setParameter("operatorName", inappAutomatedProcessRequest.getOperator().getOperatorName());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist Operator : "+isAlreadyExist);
			if (isAlreadyExist) {
				responseMap.put("message", "Operator Already Exist");
				responseMap.put("status", false);
			} else {
				daoService.saveObject(inappAutomatedProcessRequest.getOperator());
				responseMap.put("message", "Operator Added Successfully");
				responseMap.put("status", true);
			}

		}catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}


	public boolean saveAdnetworkOperatorConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
	
		try {
			queryStr = "select e.* from tb_adnetwork_operator_config e where e.ad_network_id= :adNetworkId and e.operator_id = :operatorId";
			query = entityManager.createNativeQuery(queryStr, AdnetworkOperatorConfig.class);
			query.setParameter("adNetworkId", inappAutomatedProcessRequest.getAdnetworkOperatorConfig().getAdNetworkId());
			query.setParameter("operatorId", inappAutomatedProcessRequest.getAdnetworkOperatorConfig().getOperatorId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist AdnetworkOperatorConfig : "+isAlreadyExist);
			return (isAlreadyExist)?false:daoService.saveObject(inappAutomatedProcessRequest.getAdnetworkOperatorConfig());
//			{
//			} else {
//				daoService.saveObject(inappAutomatedProcessRequest.getAdnetworkOperatorConfig());
//			}

		}catch (Exception e) {
			logger.error("ex: " + e);
		}
		return false;

	}


	public boolean saveTrafficRouting(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		
		try {
			logger.info(" TrafficRouting : "+inappAutomatedProcessRequest.getTrafficRouting());
			queryStr = "select e.* from traffic_routing e where e.campaign_id= :campaignId and e.service_id = :serviceId";
			query = entityManager.createNativeQuery(queryStr, TrafficRouting.class);
			query.setParameter("campaignId", inappAutomatedProcessRequest.getTrafficRouting().getCampaignId());
			query.setParameter("serviceId", inappAutomatedProcessRequest.getTrafficRouting().getServiceId());
			Boolean isAlreadyExist = daoService.checkExistingRecord(query);
			logger.info(" isAlreadyExist TrafficRouting : "+isAlreadyExist);
			return (isAlreadyExist) ? false:daoService.saveObject(inappAutomatedProcessRequest.getTrafficRouting());

		}catch (Exception e) {
			logger.error("ex: " + e);
		}

		return false;
	}

	public void saveServiceConfigTrans(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		ServiceConfigTrans  serviceConfigTrans = new ServiceConfigTrans();
		serviceConfigTrans.setAdvertiserid(inappAutomatedProcessRequest.getAdvertiserId());
		serviceConfigTrans.setAuthrization(inappAutomatedProcessRequest.getAuthorization());
		serviceConfigTrans.setDcturl(inappAutomatedProcessRequest.getDctUrl());
		serviceConfigTrans.setOperatorid(inappAutomatedProcessRequest.getOperatorId());
		serviceConfigTrans.setPinsendurl(inappAutomatedProcessRequest.getPinSendUrl());
		serviceConfigTrans.setPinvalidationurl(inappAutomatedProcessRequest.getPinValidationUrl());
		serviceConfigTrans.setPortalurl(inappAutomatedProcessRequest.getPortalUrl());
		serviceConfigTrans.setProductid(inappAutomatedProcessRequest.getProductId());
		serviceConfigTrans.setResendpinurl(inappAutomatedProcessRequest.getResendPinUrl());
		serviceConfigTrans.setStatuscheckurl(inappAutomatedProcessRequest.getStatusCheckUrl());
		serviceConfigTrans.setRequestStatus(inappAutomatedProcessRequest.getRequestStatus());
		serviceConfigTrans.setCreateTime(new Timestamp(System.currentTimeMillis()));
		daoService.saveObject(serviceConfigTrans);
	} 
	public Object findAllAdnetwork() {
			Map<String, Object> responseMap = new HashMap<String, Object>();
			List<Object> adnetworkList = null;
			responseMap.put("message", "Adnetworks not found");
			responseMap.put("status", false);
			try {
				queryStr = "select e.* from tb_adnetworks e";
				query = entityManager.createNativeQuery(queryStr, Adnetworks.class);
				adnetworkList= daoService.getDataList(query);
				if(adnetworkList.size()>0) {
				responseMap.put("message", "Adnetworks List");
				responseMap.put("status", true);
				responseMap.put("adnetworksList",adnetworkList);
				}
			} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
				responseMap.put("status", false);
				return JsonMapper.getObjectToJson(responseMap);
			}
			return JsonMapper.getObjectToJson(responseMap);
	 }	 
	 
	 public Object findAllAdvertiser() {
			Map<String, Object> responseMap = new HashMap<String, Object>();
			List<Object> advertiserList = null;
			responseMap.put("message", "Advertiser not found");
			responseMap.put("status", false);
			try {
				queryStr = "select e.* from tb_advertiser e";
				query = entityManager.createNativeQuery(queryStr, Advertiser.class);
				advertiserList= daoService.getDataList(query);
				if(advertiserList.size()>0) {
				responseMap.put("message", "Advertiser List");
				responseMap.put("status", true);
				responseMap.put("advertiserList",advertiserList);
				}
			} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
				responseMap.put("status", false);
				return JsonMapper.getObjectToJson(responseMap);
			}
			return JsonMapper.getObjectToJson(responseMap);
	 }

	 public Object findAllServiceConfigs() {
			Map<String, Object> responseMap = new HashMap<String, Object>();
			List<Object> advertiserList = null;
			responseMap.put("message", "Advertiser not found");
			responseMap.put("status", false);
			try {
				queryStr = "select e.* from tb_service_config_trans e";
				query = entityManager.createNativeQuery(queryStr, ServiceConfigTrans.class);
				advertiserList= daoService.getDataList(query);
				if(advertiserList.size()>0){
				responseMap.put("message", "ServiceConfig List");
				responseMap.put("status", true);
				responseMap.put("serviceConfigList",advertiserList);
				}
			} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
				responseMap.put("status", false);
				return JsonMapper.getObjectToJson(responseMap);
			}
			return JsonMapper.getObjectToJson(responseMap);
	 }
}
