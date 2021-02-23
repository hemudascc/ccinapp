package net.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

import net.mycomp.common.inapp.InappAutomatedProcessRequest;
import net.mycomp.common.inapp.InappLiveReport;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Operator;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.persist.bean.VWCampaignDetail;
import net.process.bean.AggReport;
import net.util.MConstants;

public class CommonDaoImpl extends NamedParameterJdbcTemplate implements ICommonDao {

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager entityManager;

	private static final Logger logger = Logger.getLogger(CommonDaoImpl.class);

	@Autowired
	public CommonDaoImpl(DataSource dataSource) {
		super(dataSource);
	}
	
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
		
		Query query = entityManager.createNamedQuery("Adnetworks.findAllEnableAdnetworks"
				, Adnetworks.class);
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
		TypedQuery<SubscriberReg>
		query = entityManager.createNamedQuery("SubscriberReg.searchSubscriberByLcId",SubscriberReg.class);
		query.setParameter("lifeCycleId", lifeCycleId);
		return query.getSingleResult();
	}

	@Override
	public List<Operator> findAllOperator() {
		TypedQuery<Operator>
		query = entityManager.createNamedQuery("Operator.findAllOperator",Operator.class);	
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
		TypedQuery<Operator>
		query = entityManager.createNamedQuery("Operator.findAllEnabledOperator",Operator.class);	
		return query.getResultList();
	}

	
	
	
	@Override
	public AdnetworkOperatorConfig findAdnetworkOperatorConfigById(
			Integer adnopconfigid) {
		TypedQuery<AdnetworkOperatorConfig>
		query = entityManager.createNamedQuery("AdnetworkOperatorConfig.findAdnetworkOperatorConfigById",AdnetworkOperatorConfig.class);
		query.setParameter("adnetworkOperatorConfigId", adnopconfigid);
		query.setMaxResults(1);
		return query.getSingleResult();
	}


	

	@Override
	@Transactional
	public boolean generateInappLiveReport(InappLiveReport liveReport) {
		String queryStr="INSERT INTO tb_inapp_live_report( operator_id,report_date,adnetwork_campaign_id,"
				+ "pub_id,pin_request_count, pin_send_count,pin_validation_request_count, pin_validate_count,"
				+ "pin_validate_amount,status_check_request_count,send_conversion_count,"
				+ "send_conversion_amount,action_hours,service_id,last_update_time)	"
				+ "VALUES (:operatorId,date(:reportDate),"
				+ ":adnetworkCampaignId,:pubId,"
				+ ":pinRequestCount,:pinSendCount,"
				+ ":pinValidationRequestCount,:pinValidateCount,:pinValidateAmount,"
				+ ":statusCheckRequestCount,:sendConversionCount, 	"
				+ ":sendConversionAmount,hour(:actionHours),"
				+ ":serviceId,now()) "
				+ " ON DUPLICATE KEY UPDATE "
				+" pin_request_count=pin_request_count+:pinRequestCount2,"
				+" pin_send_count=pin_send_count+:pinSendCount2,"
				+" pin_request_count=pin_request_count+:pinRequestCount2,"
				+" pin_validation_request_count=pin_validation_request_count+:pinValidationRequestCount2,"
				+" pin_validate_count=pin_validate_count+:pinValidateCount2,"
				
				+" pin_validate_amount=pin_validate_amount+:pinValidateAmount2,"
				
				+" status_check_request_count=status_check_request_count+:statusCheckRequestCount2,"
				+" send_conversion_count=send_conversion_count+:sendConversionCount2,"
				+" send_conversion_amount=send_conversion_amount+:sendConversionAmount2,"
				+ "last_update_time=now()";
		logger.debug("generateInappLiveReport::queryStr:  "+queryStr);
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
		
	//	logger.debug("generateInappLiveReport::queryStr:  "+queryStr);
		return query.executeUpdate()>0;		
		
		
	}
	
	public List<InappLiveReport> findInappLiveReportAggReport(
			AggReport aggReport){
		
Map<String, Object> parameters = new HashMap<String, Object>();
		
		String  queryStr="select if(vwlive.operator_id is not null,vwlive.operator_id,0) as operatorId,"
			     + "if(vwlive.operator_name is not null,vwlive.operator_name,'Other') as operatorName,";
		if(aggReport.getReportType()!=null&&aggReport.getReportType().equalsIgnoreCase(MConstants.MONTHLY_REPORT_TYPE)){	     
		queryStr+=  "concat(MONTHNAME(vwlive.report_date),'-',year(vwlive.report_date)) as reportDateStr,";
		}else{
			queryStr+=  "vwlive.report_date as reportDateStr,";
		}
		queryStr+=" if(vwlive.network_name is null,'Other',vwlive.network_name) as networkName,"
			     + "vwlive.adnetwork_campaign_id as adnetworkCampaignId," 
			     + "vwlive.adnetworkid as adnetworkId,"
			     + "vwlive.aggregator_id as aggregatorId,"
			     + "vws.service_id as serviceId,"
			     + "vws.service_name as serviceName,"
				 +" COALESCE(sum(vwlive.pin_request_count),0) as pinRequestCount,"
				 +" COALESCE(sum(vwlive.pin_send_count),0) as pinSendCount,"
			     + "COALESCE(sum(vwlive.pin_validation_request_count),0) as pinValidationRequestCount,"
			     + "COALESCE(sum(vwlive.pin_validate_count),0) as pinValidateCount,"
			     +"COALESCE(sum(vwlive.pin_validate_amount),0) as pinValidateAmount,"
			     //+ "round(sum(cpa.cpa_value*(vwlive.send_conversion_count)/67),2) as spend,"
			     + "COALESCE(sum(status_check_request_count),0) as statusCheckRequestCount"
			     + ",COALESCE(sum(send_conversion_count),0) as sendConversionCount"
			     +",COALESCE(sum(send_conversion_amount),0) as sendConversionAmount"
			         
			     + " from vw_inapp_live_report_monthly vwlive  join vw_service vws on vwlive.service_id=vws.service_id "
			  //   + " left join vw_service_campaign_detail vwscd "
			   //  + " on vwlive.adnetworkid =vwscd.ad_network_id and vwscd.op_id=vwlive.operator_id "
			    // + " and vwlive.service_id =vwscd.service_id "
			   //  +" left join tb_operators op on vwlive.operator_id=op.operator_id "
			     + " where  1=1  ";
		
			if(aggReport.getFromTime()!=null){
				 queryStr+= " and date(report_date)>=date(:fromTime)";
				 parameters.put("fromTime",aggReport.getFromTime());
			}//date(report_date)=date(now())
		
			if(aggReport.getToTime()!=null){
				 queryStr+= " and date(report_date)<=date(:toTime)";
				 parameters.put("toTime",aggReport.getToTime());
			}
			if(aggReport.getFromTime()==null&&aggReport.getToTime()==null){
				queryStr+= " and date(report_date)=date(now())";
			}
			
			if(aggReport.getAggregatorId()!=null){
				queryStr+= " and vwlive.aggregator_id=:aggregator_id";
				parameters.put("aggregator_id",aggReport.getAggregatorId());
			}
			
			if(aggReport.getAdnetworkId()!=null&&aggReport.getAdnetworkId()>0){
				queryStr+= " and vwlive.adnetworkid=:adnetworkid";
				parameters.put("adnetworkid",aggReport.getAdnetworkId());
			}
			if(aggReport.getOpid()!=null&&aggReport.getOpid()>0){
				queryStr+= " and vwlive.operator_id=:operator_id";
				parameters.put("operator_id",aggReport.getOpid());
			}
			if(aggReport.getProductId()!=null){
				queryStr+= " and vwscd.product_id=:product_id";
				parameters.put("product_id",aggReport.getProductId());
			}
			
		   queryStr+= " group by 1,3,5,8 order by 1 asc,3 asc";
		   
		   logger.info("query str: "+queryStr+" ,parameters:: "+parameters);
		 List<InappLiveReport> list = query(queryStr, parameters,new BeanPropertyRowMapper<InappLiveReport>(InappLiveReport.class));
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
		TypedQuery<VWAdnetworkOperatorConfig>
		query = entityManager.createNamedQuery("VWAdnetworkOperatorConfig.findAllAdnConfig",VWAdnetworkOperatorConfig.class);
		
		return query.getResultList();
	}
	
	@Override
	public List<VWCampaignDetail> findEnableVWServiceCampaignDetail() {
		TypedQuery<VWCampaignDetail>
		query = entityManager.createNamedQuery("VWServiceCampaignDetail.findEnableVWServiceCampaignDetail",VWCampaignDetail.class);
		query.setParameter("campaignDetailsStatus", true);
		query.setParameter("serviceStatus", true);
		return query.getResultList();
	}

	@Transactional
	@Override
	public List<Object> getDataList(Query query) {
		return query.getResultList();
	} 
	
	@Transactional
	@Override
	public boolean checkExistingRecord(Query query) {
		Object existingRecord = null;
		try {
		existingRecord =  query.getSingleResult();
		}catch(NoResultException e) {
			logger.error("existing config error : "+e);
		}
	    return ( ( existingRecord == null ) ? false : true );  
		
	}  

}
