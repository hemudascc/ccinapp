package net.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import net.jpa.repository.JPACountry;
import net.jpa.repository.JPACampaignDetails;
import net.jpa.repository.JPAAggregator;
import net.jpa.repository.JPAProduct;
import net.jpa.repository.JPAService;
import net.jpa.repository.JPAAdactsConfig;
import net.jpa.repository.JPAApalyaConfig;
import net.jpa.repository.JPAAscencoConfig;
import net.jpa.repository.JPACollectcentServiceConfig;
import net.jpa.repository.JPAInAppOne97Config;
import net.jpa.repository.JPAInappTmtConfig;
import net.jpa.repository.JPAKinaticConfig;
import net.jpa.repository.JPAShemarooConfig;
import net.jpa.repository.JPASkmobiConfig;
import net.jpa.repository.JPAVacaConfig;
import net.jpa.repository.JPATrafficRouting;
import net.jpa.repository.JPAOperator;
import net.jpa.repository.JPAAdnetworkOperatorConfig;
import net.mycomp.common.inapp.IInappOperatorServiceApi;
import net.mycomp.common.inapp.InappAutomatedProcessRequest;
import net.mycomp.common.inapp.InappLiveReport;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Aggregator;
import net.persist.bean.CampaignDetails;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.Service;
import net.persist.bean.TrafficRouting;
import net.mycomp.advertiser.vaca.VacaConfig;
import net.mycomp.ascenco.AscencoConfig;
import net.mycomp.common.inapp.collectcent.CollectcentServiceConfig;
import net.mycomp.common.inapp.one97.InAppOne97Config;
import net.mycomp.common.inapp.tmt.InAppTmtConfig;
import net.mycomp.inapp.adacts.AdactsConfig;
import net.mycomp.inapp.apalya.ApalyaConfig;
import net.mycomp.inapp.skmobi.SkmobiConfig;
import net.mycomp.kineticdigital.KinaticConfig;
import net.mycomp.shemaroo.ShemarooConfig;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.persist.bean.VWCampaignDetail;
import net.process.bean.AggReport;
import net.util.JsonMapper;
import net.util.MConstantAdvertiser;
import net.util.MConstants;

public class CommonDaoImpl extends NamedParameterJdbcTemplate implements ICommonDao {

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager entityManager;

	private static final Logger logger = Logger.getLogger(CommonDaoImpl.class);

	@Autowired
	public CommonDaoImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Autowired
	JPACountry jpacountry;

	@Autowired
	JPAAdactsConfig jpaadactsconfig;

	@Autowired
	JPAAggregator jpaaggregator;

	@Autowired
	JPAProduct jpaproduct;

	@Autowired
	JPAService jpaservice;

	@Autowired
	JPACampaignDetails jpacampaigndetails;

//	@Autowired
//	JPAAdactsConfig jpaadactsconfig;

	@Autowired
	JPAKinaticConfig jpakinaticconfig;

	@Autowired
	JPAShemarooConfig jpashemarooconfig;

	@Autowired
	JPASkmobiConfig jpaskmobiconfig;

	@Autowired
	JPAVacaConfig jpavacaconfig;

	@Autowired
	JPAApalyaConfig jpaapalyaconfig;

	@Autowired
	JPATrafficRouting jpatrafficRouting;

	@Autowired
	JPAAscencoConfig jpaascencoconfig;

	@Autowired
	JPACollectcentServiceConfig jpacollectcentserviceconfig;

	@Autowired
	JPAInAppOne97Config jpainappone97config;

	@Autowired
	JPAInappTmtConfig jpainapptmtconfig;

	@Autowired
	JPAOperator jpaoperator;

	@Autowired
	JPAAdnetworkOperatorConfig Jpaadnetworkoperatorconfig;

	@Transactional
	@Override
	public boolean saveObject(Object object) {
		entityManager.persist(object);
		return true;
	}

	@Transactional
	@Override
	public boolean updateObject(Object object) {
		entityManager.merge(object);
		return true;
	}

	@Override
	public List<AdnetworkOperatorConfig> findAdnetworkOperatorConfig() {

		String queryStr = "select b.* from tb_adnetwork_operator_config b";

		Query query = entityManager.createNativeQuery(queryStr, AdnetworkOperatorConfig.class);
		return query.getResultList();
	}

	@Override
	public List<Adnetworks> findAllEnableAdnetworks() {

		Query query = entityManager.createNamedQuery("Adnetworks.findAllEnableAdnetworks", Adnetworks.class);
		query.setParameter("status", true);
		return query.getResultList();
	}

	@Override
	public Integer findNextAutoIncrementId(String tableName, String dbName) {

		String sqlString = "SELECT AUTO_INCREMENT FROM  INFORMATION_SCHEMA.TABLES "
				+ " WHERE TABLE_SCHEMA =:dbName  AND   TABLE_NAME   = :tableName ;";
		Map<String, String> map = new HashMap<String, String>();
		map.put("dbName", dbName);
		map.put("tableName", tableName);
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(map);
		return queryForObject(sqlString, sqlParameterSource, Integer.class);
	}

	@Override
	public SubscriberReg searchSubscriberByLcId(String lifeCycleId) {
		TypedQuery<SubscriberReg> query = entityManager.createNamedQuery("SubscriberReg.searchSubscriberByLcId",
				SubscriberReg.class);
		query.setParameter("lifeCycleId", lifeCycleId);
		return query.getSingleResult();
	}

	@Override
	public List<Operator> findAllOperator() {
		TypedQuery<Operator> query = entityManager.createNamedQuery("Operator.findAllOperator", Operator.class);
		return query.getResultList();
	}

//	@Override
//	public List<VWServiceCampaignDetail> findEnableVWServiceCampaignDetail() {
//		TypedQuery<VWServiceCampaignDetail>
//		query = entityManager.createNamedQuery("VWServiceCampaignDetail.findEnableVWServiceCampaignDetail",VWServiceCampaignDetail.class);
//		query.setParameter("campaignDetailsStatus", true);
//		query.setParameter("serviceStatus", true);
//		return query.getResultList();
//	}

	@Override
	public List<Operator> findAllEnabledOperator() {
		TypedQuery<Operator> query = entityManager.createNamedQuery("Operator.findAllEnabledOperator", Operator.class);
		return query.getResultList();
	}

	@Override
	public AdnetworkOperatorConfig findAdnetworkOperatorConfigById(Integer adnopconfigid) {
		TypedQuery<AdnetworkOperatorConfig> query = entityManager.createNamedQuery(
				"AdnetworkOperatorConfig.findAdnetworkOperatorConfigById", AdnetworkOperatorConfig.class);
		query.setParameter("adnetworkOperatorConfigId", adnopconfigid);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	@Override
	@Transactional
	public boolean generateInappLiveReport(InappLiveReport liveReport) {
		String queryStr = "INSERT INTO tb_inapp_live_report( operator_id,report_date,adnetwork_campaign_id,"
				+ "pub_id,pin_request_count, pin_send_count,pin_validation_request_count, pin_validate_count,"
				+ "pin_validate_amount,status_check_request_count,send_conversion_count,"
				+ "send_conversion_amount,action_hours,service_id,last_update_time)	"
				+ "VALUES (:operatorId,date(:reportDate)," + ":adnetworkCampaignId,:pubId,"
				+ ":pinRequestCount,:pinSendCount," + ":pinValidationRequestCount,:pinValidateCount,:pinValidateAmount,"
				+ ":statusCheckRequestCount,:sendConversionCount, 	" + ":sendConversionAmount,hour(:actionHours),"
				+ ":serviceId,now()) " + " ON DUPLICATE KEY UPDATE "
				+ " pin_request_count=pin_request_count+:pinRequestCount2,"
				+ " pin_send_count=pin_send_count+:pinSendCount2,"
				+ " pin_request_count=pin_request_count+:pinRequestCount2,"
				+ " pin_validation_request_count=pin_validation_request_count+:pinValidationRequestCount2,"
				+ " pin_validate_count=pin_validate_count+:pinValidateCount2,"

				+ " pin_validate_amount=pin_validate_amount+:pinValidateAmount2,"

				+ " status_check_request_count=status_check_request_count+:statusCheckRequestCount2,"
				+ " send_conversion_count=send_conversion_count+:sendConversionCount2,"
				+ " send_conversion_amount=send_conversion_amount+:sendConversionAmount2," + "last_update_time=now()";
		logger.debug("generateInappLiveReport::queryStr:  " + queryStr);
		Query query = entityManager.createNativeQuery(queryStr);
		query.setParameter("operatorId", liveReport.getOperatorId());

		query.setParameter("reportDate", liveReport.getReportDate());
		query.setParameter("adnetworkCampaignId", liveReport.getAdnetworkCampaignId());

		query.setParameter("pubId", liveReport.getPubId());

		query.setParameter("pinRequestCount", liveReport.getPinRequestCount());
		query.setParameter("pinRequestCount2", liveReport.getPinRequestCount());

		query.setParameter("pinSendCount", liveReport.getPinSendCount());
		query.setParameter("pinSendCount2", liveReport.getPinSendCount());

		query.setParameter("pinValidationRequestCount", liveReport.getPinValidationRequestCount());
		query.setParameter("pinValidationRequestCount2", liveReport.getPinValidationRequestCount());

		query.setParameter("pinValidateCount", liveReport.getPinValidateCount());
		query.setParameter("pinValidateCount2", liveReport.getPinValidateCount());

		query.setParameter("pinValidateAmount", liveReport.getPinValidateAmount());
		query.setParameter("pinValidateAmount2", liveReport.getPinValidateAmount());

		query.setParameter("statusCheckRequestCount", liveReport.getStatusCheckRequestCount());
		query.setParameter("statusCheckRequestCount2", liveReport.getStatusCheckRequestCount());

		query.setParameter("sendConversionCount", liveReport.getSendConversionCount());
		query.setParameter("sendConversionCount2", liveReport.getSendConversionCount());

		query.setParameter("sendConversionAmount", liveReport.getSendConversionAmount());
		query.setParameter("sendConversionAmount2", liveReport.getSendConversionAmount());

		query.setParameter("actionHours", liveReport.getReportDate());
		query.setParameter("serviceId", liveReport.getServiceId());

		// logger.debug("generateInappLiveReport::queryStr: "+queryStr);
		return query.executeUpdate() > 0;

	}

	public List<InappLiveReport> findInappLiveReportAggReport(AggReport aggReport) {

		Map<String, Object> parameters = new HashMap<String, Object>();

		String queryStr = "select if(vwlive.operator_id is not null,vwlive.operator_id,0) as operatorId,"
				+ "if(vwlive.operator_name is not null,vwlive.operator_name,'Other') as operatorName,";
		if (aggReport.getReportType() != null
				&& aggReport.getReportType().equalsIgnoreCase(MConstants.MONTHLY_REPORT_TYPE)) {
			queryStr += "concat(MONTHNAME(vwlive.report_date),'-',year(vwlive.report_date)) as reportDateStr,";
		} else {
			queryStr += "vwlive.report_date as reportDateStr,";
		}
		queryStr += " if(vwlive.network_name is null,'Other',vwlive.network_name) as networkName,"
				+ "vwlive.adnetwork_campaign_id as adnetworkCampaignId," + "vwlive.adnetworkid as adnetworkId,"
				+ "vwlive.aggregator_id as aggregatorId," + "vws.service_id as serviceId,"
				+ "vws.service_name as serviceName," + " COALESCE(sum(vwlive.pin_request_count),0) as pinRequestCount,"
				+ " COALESCE(sum(vwlive.pin_send_count),0) as pinSendCount,"
				+ "COALESCE(sum(vwlive.pin_validation_request_count),0) as pinValidationRequestCount,"
				+ "COALESCE(sum(vwlive.pin_validate_count),0) as pinValidateCount,"
				+ "COALESCE(sum(vwlive.pin_validate_amount),0) as pinValidateAmount,"
				// + "round(sum(cpa.cpa_value*(vwlive.send_conversion_count)/67),2) as spend,"
				+ "COALESCE(sum(status_check_request_count),0) as statusCheckRequestCount"
				+ ",COALESCE(sum(send_conversion_count),0) as sendConversionCount"
				+ ",COALESCE(sum(send_conversion_amount),0) as sendConversionAmount"

				+ " from vw_inapp_live_report_monthly vwlive  join vw_service vws on vwlive.service_id=vws.service_id "
				// + " left join vw_service_campaign_detail vwscd "
				// + " on vwlive.adnetworkid =vwscd.ad_network_id and
				// vwscd.op_id=vwlive.operator_id "
				// + " and vwlive.service_id =vwscd.service_id "
				// +" left join tb_operators op on vwlive.operator_id=op.operator_id "
				+ " where  1=1  ";

		if (aggReport.getFromTime() != null) {
			queryStr += " and date(report_date)>=date(:fromTime)";
			parameters.put("fromTime", aggReport.getFromTime());
		} // date(report_date)=date(now())

		if (aggReport.getToTime() != null) {
			queryStr += " and date(report_date)<=date(:toTime)";
			parameters.put("toTime", aggReport.getToTime());
		}
		if (aggReport.getFromTime() == null && aggReport.getToTime() == null) {
			queryStr += " and date(report_date)=date(now())";
		}

		if (aggReport.getAggregatorId() != null) {
			queryStr += " and vwlive.aggregator_id=:aggregator_id";
			parameters.put("aggregator_id", aggReport.getAggregatorId());
		}

		if (aggReport.getAdnetworkId() != null && aggReport.getAdnetworkId() > 0) {
			queryStr += " and vwlive.adnetworkid=:adnetworkid";
			parameters.put("adnetworkid", aggReport.getAdnetworkId());
		}
		if (aggReport.getOpid() != null && aggReport.getOpid() > 0) {
			queryStr += " and vwlive.operator_id=:operator_id";
			parameters.put("operator_id", aggReport.getOpid());
		}
		if (aggReport.getProductId() != null) {
			queryStr += " and vwscd.product_id=:product_id";
			parameters.put("product_id", aggReport.getProductId());
		}

		queryStr += " group by 1,3,5,8 order by 1 asc,3 asc";

		logger.info("query str: " + queryStr + " ,parameters:: " + parameters);
		List<InappLiveReport> list = query(queryStr, parameters,
				new BeanPropertyRowMapper<InappLiveReport>(InappLiveReport.class));
		return list;

	}

	@Override
	public SubscriberReg searchSubscriber(String msisdn) {

		Query query = entityManager.createNamedQuery("SubscriberReg.findSubscriberRegByMsisdn", SubscriberReg.class);
		query.setParameter("msisdn", msisdn);
		SubscriberReg subscriberReg = (SubscriberReg) query.getSingleResult();
		return subscriberReg;
	}

	@Override
	public List<VWAdnetworkOperatorConfig> findAllAdnConfig() {
		TypedQuery<VWAdnetworkOperatorConfig> query = entityManager
				.createNamedQuery("VWAdnetworkOperatorConfig.findAllAdnConfig", VWAdnetworkOperatorConfig.class);

		return query.getResultList();
	}

	@Override
	public List<VWCampaignDetail> findEnableVWServiceCampaignDetail() {
		TypedQuery<VWCampaignDetail> query = entityManager
				.createNamedQuery("VWServiceCampaignDetail.findEnableVWServiceCampaignDetail", VWCampaignDetail.class);
		query.setParameter("campaignDetailsStatus", true);
		query.setParameter("serviceStatus", true);
		return query.getResultList();
	}

	/* In App Automated API methods */

	@Override
	public Object findAllCountry() {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Country> countryList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Country Not Found");
		responseMap.put("status", "Fail");
		try {
			countryList = jpacountry.findAll();
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
		List<Product> productList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Product Not Found");
		responseMap.put("status", "Fail");
		try {
			productList = jpaproduct.findAll();
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Product List");
			responseMap.put("status", "Success");
			responseMap.put("productList", "" + productList);
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
		List<Aggregator> aggregatorList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Aggregator not added");
		responseMap.put("status", "Fail");
		try {
			jpaaggregator.findAll();
			responseMap.put("statusCode", "500");
			responseMap.put("message", "Aggregator List");
			responseMap.put("status", "Fail");
			responseMap.put("aggregatorList", "" + aggregatorList);
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
		List<Service> serivceList = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Service not added");
		responseMap.put("status", "Fail");
		try {
			serivceList = jpaservice.findAll();
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Service List");
			responseMap.put("status", "Success");
			responseMap.put("serivceList", "" + serivceList);
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
		List<CampaignDetails> campaignDetails = null;
		responseMap.put("statusCode", "500");
		responseMap.put("message", "CampaignDetails not added");
		responseMap.put("status", "Fail");
		try {
			campaignDetails = jpacampaigndetails.findAll();
			responseMap.put("statusCode", "200");
			responseMap.put("message", "CampaignDetails List");
			responseMap.put("status", "Success");
			responseMap.put("campaignDetails", "" + campaignDetails);
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveCampaignDetails(CampaignDetails campaigndetails) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Campaign Details not added");
		responseMap.put("status", "Fail");
		try {
			CampaignDetails dbcampaigndetails = jpacampaigndetails.findByCampaignNameAndAdNetworkIdAndOpId(
					campaigndetails.getCampaignName(), campaigndetails.getAdNetworkId(), campaigndetails.getOpId());
			if (dbcampaigndetails.getCampaignName().equalsIgnoreCase(campaigndetails.getCampaignName())) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Campaign Details Already Exist");
				responseMap.put("status", "Fail");
			} else {
				jpacampaigndetails.save(campaigndetails);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Campaign Details added Successfully");
				responseMap.put("status", "Success");
			}
		} catch (NullPointerException ne) {
			logger.info("ex: " + ne);
			jpacampaigndetails.save(campaigndetails);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Campaign Details added Successfully");
			responseMap.put("status", "Success");
			return JsonMapper.getObjectToJson(responseMap);
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveCountry(Country country) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Country not added");
		responseMap.put("status", "Fail");
		try {
			Country dbcountry = jpacountry.findByName(country.getName());
			if (dbcountry.getName().equalsIgnoreCase(country.getName())) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Country Already Exist");
				responseMap.put("status", "Success");
			} else {

				jpacountry.save(country);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Country Added Successfully");
				responseMap.put("status", "Success");
			}
		} catch (NullPointerException ne) {
			logger.info("ex: " + ne+" Name "+country.getName()+" status: "+country.getStatus());
			jpacountry.save(country);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Country Added Successfully");
			responseMap.put("status", "Success");
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Transactional
	@Override
	public Object saveProduct(Product product) {

		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Product not added");
		responseMap.put("status", "Fail");
		try {
			Product pr = jpaproduct.findByProductName(product.getProductName());
			logger.info("pr: " + pr.getProductName() + "  ppr:  " + product.getProductName());
			if (pr.getProductName().equalsIgnoreCase(product.getProductName())) {

				responseMap.put("statusCode", "200");
				responseMap.put("message", "Product Already Exist");
				responseMap.put("status", "Success");

//				product.setId(pr.getId());
//				product.setProductName(pr.getProductName());
//				product.setStatus(product.getStatus());
//				Boolean status = updateObject(product);
//				logger.info("status : " + status);
//				if (status) {
//					responseMap.put("statusCode", "200");
//					responseMap.put("message", "Product updated");
//					responseMap.put("status", "Success");
//				}
			} else {
				jpaproduct.save(product);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Product Added Successfully");
				responseMap.put("status", "Success");
			}
		} catch (NullPointerException ne) {
			logger.info("ex: " + ne);
//			if (saveObject(product)) {
			jpaproduct.save(product);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Product Added Successfully");
				responseMap.put("status", "Success");
				return JsonMapper.getObjectToJson(responseMap);
//			}
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);
	}

	@Override
	public Object saveAggregator(Aggregator aggregator) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Aggregator not added");
		responseMap.put("status", "Fail");
		try {
			Aggregator dbaggregator = jpaaggregator.findByName(aggregator.getName());
			if (dbaggregator.getName().equalsIgnoreCase(aggregator.getName())) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Aggregator Already Exist");
				responseMap.put("status", "Success");
			} else {
				jpaaggregator.save(aggregator);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Aggregator added Successfully");
				responseMap.put("status", "Success");
			}
		} catch (NullPointerException ne) {
			logger.info("ex: " + ne);
			jpaaggregator.save(aggregator);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "AdNetwork Operator Config Added Successfully");
			responseMap.put("status", "Success");
			return JsonMapper.getObjectToJson(responseMap);
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveSerivce(Service service) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Service not added");
		responseMap.put("status", "Fail");
		try {
			Service dbservice = jpaservice.findByAdvertiserIdAndServiceNameAndOpId(service.getAdvertiserId(),
					service.getServiceName(), service.getOpId());
			if (dbservice.getServiceName().equalsIgnoreCase(service.getServiceName())) {

				responseMap.put("statusCode", "200");
				responseMap.put("message", "Service Already Exist");
				responseMap.put("status", "Success");
			} else {
				jpaservice.save(service);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Service Added Successfully");
				responseMap.put("status", "Success");
			}
		} catch (NullPointerException ne) {
			logger.info("ex: " + ne);
			jpaservice.save(service);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Service Added Successfully");
			responseMap.put("status", "Success");
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveOperator(Operator operator) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "Operator not added");
		responseMap.put("status", "Fail");
		try {
			Operator op = jpaoperator.findByOperatorNameAndAggregatorIdAndCountryId(operator.getOperatorName(),
					operator.getAggregatorId(), operator.getCountryId());
			logger.info("op : " + op.getOperatorName());
			if (operator.getOperatorName().equalsIgnoreCase(op.getOperatorName())) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Operator Already Exist");
				responseMap.put("status", "Success");
			} else {
				jpaoperator.save(operator);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "Operator Added Successfully");
				responseMap.put("status", "Success");
			}

		} catch (NullPointerException ne) {
			logger.info("ex: " + ne);
			jpaoperator.save(operator);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "Operator Added Successfully");
			responseMap.put("status", "Success");
			return JsonMapper.getObjectToJson(responseMap);
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveAdnetworkOperatorConfig(AdnetworkOperatorConfig adnetworkoperatorconfig) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "AdNetwork Operator Config Not Added");
		responseMap.put("status", "Fail");
		try {
			AdnetworkOperatorConfig adnetworkoperatorconfigdb = Jpaadnetworkoperatorconfig
					.findByOperatorIdAndAdNetworkId(adnetworkoperatorconfig.getOperatorId(),
							adnetworkoperatorconfig.getAdNetworkId());
			if (adnetworkoperatorconfigdb.getOperatorId() == adnetworkoperatorconfig.getOperatorId()) {

				responseMap.put("statusCode", "200");
				responseMap.put("message", "AdNetwork Operator Config Already Exist");
				responseMap.put("status", "Success");
			} else {
				Jpaadnetworkoperatorconfig.save(adnetworkoperatorconfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "AdNetwork Operator Config Added Successfully");
				responseMap.put("status", "Success");
			}

		} catch (NullPointerException ne) {
			logger.info("ex: " + ne);
			Jpaadnetworkoperatorconfig.save(adnetworkoperatorconfig);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "AdNetwork Operator Config Added Successfully");
			responseMap.put("status", "Success");
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
			return JsonMapper.getObjectToJson(responseMap);
		}
		return JsonMapper.getObjectToJson(responseMap);

	}

	@Override
	public Object saveTrafficRouting(TrafficRouting trafficrouting) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "TrafficRouting not added");
		responseMap.put("status", "Fail");

		try {
			TrafficRouting dbtrafficrouting = jpatrafficRouting.findByCampaignIdAndServiceId(trafficrouting.getCampaignId(),trafficrouting.getServiceId());
			if (dbtrafficrouting != null) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "TrafficRouting Already Exist");
				responseMap.put("status", "Success");
			}else {
				jpatrafficRouting.save(trafficrouting);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "TrafficRouting added Successfully");
				responseMap.put("status", "Success");
			}
		
		} catch (NullPointerException ne) {
			logger.error("ex: " + ne);
			jpatrafficRouting.save(trafficrouting);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "TrafficRouting added Successfully");
			responseMap.put("status", "Success");
		} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
		}

		return JsonMapper.getObjectToJson(responseMap);
	}
	
	@Override
	public Object advertiserConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Object res = null;
		switch (inappAutomatedProcessRequest.getAdvertiserId()) {
		case MConstantAdvertiser.TMT: {
			res = saveInAppTmtConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.ONE97: {
			res = saveInAppOne97Config(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.COLLECTCENT: {
			res = saveCollectcentServiceConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.SKMOBI: {
			res = saveSkmobiConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.SHEMAROO: {
			res = saveShemarooConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.KINATIC: {
			res = saveKinaticConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.ASCENCO: {
			res = saveAscencoConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.VACA: {
			res = saveVacaConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.ADACTS: {
			res = saveAdactsConfig(inappAutomatedProcessRequest);
			break;
		}
		case MConstantAdvertiser.APALYA: {
			res = saveApalyaConfig(inappAutomatedProcessRequest);
			break;
		}
		default:{
//			inappPublisherService=defaultInappPublisherService;
			res = defaultAdvertiserRes(inappAutomatedProcessRequest);
			break;
		 }
		}
		return res;
	}

	public Object saveAdactsConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "AdactsConfig not added");
		responseMap.put("status", "Fail");

		AdactsConfig adactsConfig = new AdactsConfig();
		adactsConfig.setId(inappAutomatedProcessRequest.getId());
		adactsConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		adactsConfig.setProductId(inappAutomatedProcessRequest.getProductId());
		adactsConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
		adactsConfig.setCampId(inappAutomatedProcessRequest.getCampId());
		adactsConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
		adactsConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
		adactsConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
		adactsConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		adactsConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
		adactsConfig.setStatus(inappAutomatedProcessRequest.getStatus());
		try {
			AdactsConfig dbadactsConfig = jpaadactsconfig.findByServiceIdAndProductIdAndOperatorNameAndCampId(adactsConfig.getServiceId(),adactsConfig.getProductId(),adactsConfig.getOperatorName(),adactsConfig.getCampId());
			if(dbadactsConfig !=null) {
				responseMap.put("statusCode", "200");
				responseMap.put("message", "AdactsConfig Already Exist");
				responseMap.put("status", "Success");
			}else {
			jpaadactsconfig.save(adactsConfig);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "AdactsConfig added Successfully");
			responseMap.put("status", "Success");
			}
		} catch (NullPointerException ne) {
			logger.error("ex: " + ne);
			jpaadactsconfig.save(adactsConfig);
			responseMap.put("statusCode", "200");
			responseMap.put("message", "AdactsConfig added Successfully");
			responseMap.put("status", "Success");
			} catch (Exception e) {
			logger.error("ex: " + e);
			responseMap.put("message", "" + e);
		}

		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveShemarooConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "ShemarooConfig not added");
		responseMap.put("status", "Fail");

		ShemarooConfig shemarooConfig = new ShemarooConfig();
		shemarooConfig.setId(inappAutomatedProcessRequest.getId());
		shemarooConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		shemarooConfig.setProductId(inappAutomatedProcessRequest.getProductId());
		shemarooConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
		shemarooConfig.setCampId(inappAutomatedProcessRequest.getCampId());
		shemarooConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
		shemarooConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
		shemarooConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
		shemarooConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		shemarooConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
		shemarooConfig.setStatus(inappAutomatedProcessRequest.getStatus());

		
			try {
				ShemarooConfig dbshemarooConfig = jpashemarooconfig.findByServiceIdAndProductIdAndOperatorNameAndCampId(shemarooConfig.getServiceId(),shemarooConfig.getProductId(),shemarooConfig.getOperatorName(),shemarooConfig.getCampId());
				if(dbshemarooConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "ShemarooConfig Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpashemarooconfig.save(shemarooConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "ShemarooConfig added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpashemarooconfig.save(shemarooConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "ShemarooConfig added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}
		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveKinaticConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "KinaticConfig not added");
		responseMap.put("status", "Fail");

		KinaticConfig kinaticConfig = new KinaticConfig();
		kinaticConfig.setId(inappAutomatedProcessRequest.getId());
		kinaticConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		kinaticConfig.setProductId(inappAutomatedProcessRequest.getProductId());
		kinaticConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
		kinaticConfig.setCampId(inappAutomatedProcessRequest.getCampId());
		kinaticConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
		kinaticConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
		kinaticConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
		kinaticConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		kinaticConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
		kinaticConfig.setStatus(inappAutomatedProcessRequest.getStatus());

			try {
				KinaticConfig dbkinaticConfig = jpakinaticconfig.findByServiceIdAndProductIdAndOperatorNameAndCampId(kinaticConfig.getServiceId(),kinaticConfig.getProductId(),kinaticConfig.getOperatorName(),kinaticConfig.getCampId());
				if(dbkinaticConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "KinaticConfig Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpakinaticconfig.save(kinaticConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "KinaticConfig added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpakinaticconfig.save(kinaticConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "KinaticConfig added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}
		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveSkmobiConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "SkmobiConfig not added");
		responseMap.put("status", "Fail");

		SkmobiConfig skmobiConfig = new SkmobiConfig();
		skmobiConfig.setId(inappAutomatedProcessRequest.getId());
		skmobiConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		skmobiConfig.setProductId(inappAutomatedProcessRequest.getProductId());
		skmobiConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
		skmobiConfig.setCampId(inappAutomatedProcessRequest.getCampId());
		skmobiConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
		skmobiConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
		skmobiConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
		skmobiConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		skmobiConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
		skmobiConfig.setStatus(inappAutomatedProcessRequest.getStatus());

			try {
				SkmobiConfig dbskmobiConfig = jpaskmobiconfig.findByServiceIdAndProductIdAndOperatorNameAndCampId(skmobiConfig.getServiceId(),skmobiConfig.getProductId(),skmobiConfig.getOperatorName(),skmobiConfig.getCampId());
				if(dbskmobiConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "SkmobiConfig Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpaskmobiconfig.save(skmobiConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "SkmobiConfig added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpaskmobiconfig.save(skmobiConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "SkmobiConfig added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}

		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveApalyaConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "ApalyaConfig not added");
		responseMap.put("status", "Fail");

		ApalyaConfig apalyaConfig = new ApalyaConfig();
		apalyaConfig.setId(inappAutomatedProcessRequest.getId());
		apalyaConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		apalyaConfig.setProductId(inappAutomatedProcessRequest.getProductId());
		apalyaConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
		apalyaConfig.setCampId(inappAutomatedProcessRequest.getCampId());
		apalyaConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
		apalyaConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
		apalyaConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
		apalyaConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		apalyaConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
		apalyaConfig.setStatus(inappAutomatedProcessRequest.getStatus());

			try {
				ApalyaConfig dbapalyaConfig = jpaapalyaconfig.findByServiceIdAndProductIdAndOperatorNameAndCampId(apalyaConfig.getServiceId(),apalyaConfig.getProductId(),apalyaConfig.getOperatorName(),apalyaConfig.getCampId());
				if(dbapalyaConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "ApalyaConfig Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpaapalyaconfig.save(apalyaConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "ApalyaConfig added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpaapalyaconfig.save(apalyaConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "ApalyaConfig added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}
		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveInAppTmtConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {

		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "InAppTmtConfig not added");
		responseMap.put("status", "Fail");

		InAppTmtConfig inAppTmtConfig = new InAppTmtConfig();
		inAppTmtConfig.setId(inappAutomatedProcessRequest.getId());
		inAppTmtConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		inAppTmtConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		inAppTmtConfig.setStatus(inappAutomatedProcessRequest.getStatus());
		inAppTmtConfig.setPinSendUrl(inappAutomatedProcessRequest.getPinSendUrl());
		inAppTmtConfig.setPinValidationUrl(inappAutomatedProcessRequest.getPinValidationUrl());
		inAppTmtConfig.setCheckSubUrl(inappAutomatedProcessRequest.getCheckSubUrl());
		inAppTmtConfig.setPortalUrl2(inappAutomatedProcessRequest.getPortalUrl2());
		inAppTmtConfig.setAmount(inappAutomatedProcessRequest.getAmount());

			try {
				InAppTmtConfig dbinAppTmtConfig = jpainapptmtconfig.findByServiceIdAndOperatorName(inAppTmtConfig.getServiceId(),inAppTmtConfig.getOperatorName());
				if(dbinAppTmtConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "InAppTmtConfig Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpainapptmtconfig.save(inAppTmtConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "InAppTmtConfig added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpainapptmtconfig.save(inAppTmtConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "InAppTmtConfig added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}
		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveInAppOne97Config(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "InAppOne97Config not added");
		responseMap.put("status", "Fail");

		InAppOne97Config inAppOne97Config = new InAppOne97Config();
		inAppOne97Config.setId(inappAutomatedProcessRequest.getId());
		inAppOne97Config.setServiceId(inappAutomatedProcessRequest.getServiceId());
		inAppOne97Config.setOperatorDetail(inappAutomatedProcessRequest.getOperatorDetails());
		inAppOne97Config.setPinSendUrl(inappAutomatedProcessRequest.getPinSendUrl());
		inAppOne97Config.setPinValidationUrl(inappAutomatedProcessRequest.getPinValidationUrl());
		inAppOne97Config.setCheckSubUrl(inappAutomatedProcessRequest.getCheckSubUrl());
		inAppOne97Config.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		inAppOne97Config.setAmount(inappAutomatedProcessRequest.getAmount());
		inAppOne97Config.setStatus(inappAutomatedProcessRequest.getStatus());

			try {
				InAppOne97Config dbinAppOne97Config = jpainappone97config.findByServiceIdAndOperatorDetail(inAppOne97Config.getServiceId(),inAppOne97Config.getOperatorDetail());
				if(dbinAppOne97Config !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "InAppOne97Config Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpainappone97config.save(inAppOne97Config);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "InAppOne97Config added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpainappone97config.save(inAppOne97Config);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "InAppOne97Config added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}

		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveCollectcentServiceConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "CollectcentServiceConfig not added");
		responseMap.put("status", "Fail");

		CollectcentServiceConfig collectcentServiceConfig = new CollectcentServiceConfig();
		collectcentServiceConfig.setId(inappAutomatedProcessRequest.getId());
		collectcentServiceConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		collectcentServiceConfig.setPinSendUrl(inappAutomatedProcessRequest.getPinSendUrl());
		collectcentServiceConfig.setPinValidationUrl(inappAutomatedProcessRequest.getPinValidationUrl());
		collectcentServiceConfig.setCheckSubUrl(inappAutomatedProcessRequest.getCheckSubUrl());
		collectcentServiceConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		collectcentServiceConfig.setDctUrl(inappAutomatedProcessRequest.getDctUrl());
		collectcentServiceConfig.setAmount(inappAutomatedProcessRequest.getAmount());
		collectcentServiceConfig.setStatus(inappAutomatedProcessRequest.getStatus());

		try {
			CollectcentServiceConfig dbcollectcentServiceConfig = jpacollectcentserviceconfig.findByServiceId(collectcentServiceConfig.getServiceId());
				if(dbcollectcentServiceConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "CollectcentServiceConfig Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpacollectcentserviceconfig.save(collectcentServiceConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "CollectcentServiceConfig added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpacollectcentserviceconfig.save(collectcentServiceConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "CollectcentServiceConfig added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}
		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveAscencoConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "AscencoConfig not added");
		responseMap.put("status", "Fail");

		AscencoConfig ascencoConfig = new AscencoConfig();
		ascencoConfig.setId(inappAutomatedProcessRequest.getId());
		ascencoConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		ascencoConfig.setProductId(inappAutomatedProcessRequest.getProductId());
		ascencoConfig.setAuthorization(inappAutomatedProcessRequest.getAuthorization());
		ascencoConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
		ascencoConfig.setCampId(inappAutomatedProcessRequest.getCampId());
		ascencoConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
		ascencoConfig.setResendPinUrl(inappAutomatedProcessRequest.getResendPinUrl());
		ascencoConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
		ascencoConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
		ascencoConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		ascencoConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
		ascencoConfig.setStatus(inappAutomatedProcessRequest.getStatus());
			try {
				AscencoConfig dbascencoConfig = jpaascencoconfig.findByServiceIdAndProductIdAndOperatorNameAndCampId(ascencoConfig.getServiceId(),ascencoConfig.getProductId(),ascencoConfig.getOperatorName(),ascencoConfig.getCampId());
				if(dbascencoConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "AscencoConfig Already Exist");
					responseMap.put("status", "Success");
				}else {

					jpaascencoconfig.save(ascencoConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "AscencoConfig added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);

				jpaascencoconfig.save(ascencoConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "AscencoConfig added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}

		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object saveVacaConfig(InappAutomatedProcessRequest inappAutomatedProcessRequest) {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("statusCode", "500");
		responseMap.put("message", "VacaConfig not added");
		responseMap.put("status", "Fail");

		VacaConfig vacaConfig = new VacaConfig();
		vacaConfig.setId(inappAutomatedProcessRequest.getId());
		vacaConfig.setServiceId(inappAutomatedProcessRequest.getServiceId());
		vacaConfig.setProductId(inappAutomatedProcessRequest.getProductId());
		vacaConfig.setOperatorName(inappAutomatedProcessRequest.getOperatorName());
//		vacaConfig.setCampId(inappAutomatedProcessRequest.getCampId());	
		vacaConfig.setPinGenerationUrl(inappAutomatedProcessRequest.getPinGenerationUrl());
		vacaConfig.setResendPinUrl(inappAutomatedProcessRequest.getResendPinUrl());
		vacaConfig.setPinVerificationUrl(inappAutomatedProcessRequest.getPinVerificationUrl());
		vacaConfig.setStatusCheckUrl(inappAutomatedProcessRequest.getStatusCheckUrl());
		vacaConfig.setPortalUrl(inappAutomatedProcessRequest.getPortalUrl());
		vacaConfig.setPricePoint(inappAutomatedProcessRequest.getPricePoint());
		vacaConfig.setStatus(inappAutomatedProcessRequest.getStatus());

			try {
				VacaConfig dbvacaConfig = jpavacaconfig.findByServiceIdAndProductIdAndOperatorName(vacaConfig.getServiceId(),vacaConfig.getProductId(),vacaConfig.getOperatorName());
				if(dbvacaConfig !=null) {
					responseMap.put("statusCode", "200");
					responseMap.put("message", "VacaConfig Already Exist");
					responseMap.put("status", "Success");
				}else {
					jpavacaconfig.save(vacaConfig);
					responseMap.put("statusCode", "200");
					responseMap.put("message", "VacaConfig Added Successfully");
					responseMap.put("status", "Success");
				}
			} catch (NullPointerException ne) {
				logger.error("ex: " + ne);
				jpavacaconfig.save(vacaConfig);
				responseMap.put("statusCode", "200");
				responseMap.put("message", "VacaConfig Added Successfully");
				responseMap.put("status", "Success");
				} catch (Exception e) {
				logger.error("ex: " + e);
				responseMap.put("message", "" + e);
			}
		return JsonMapper.getObjectToJson(responseMap);
	}

	public Object defaultAdvertiserRes(InappAutomatedProcessRequest inappAutomatedProcessRequest){
		
		Map<String, String> responseMap = new HashMap<String, String>();

			responseMap.put("statusCode", "200");
			responseMap.put("message", "Advertiser does not exists for given Advertiser Id");
			responseMap.put("status", "Success");	
		
		return JsonMapper.getObjectToJson(responseMap);

	}

}
