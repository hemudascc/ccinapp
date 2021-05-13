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

import net.mycomp.common.inapp.InAppAdverterReport;
import net.mycomp.common.inapp.InappLiveReport;
import net.mycomp.common.inapp.UniqueCount;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Operator;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.persist.bean.VWCampaignDetail;
import net.process.bean.AggReport;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

public class CommonDaoImpl extends NamedParameterJdbcTemplate implements ICommonDao {
   @PersistenceContext(
      unitName = "entityManagerFactory"
   )
   private EntityManager entityManager;
   private static final Logger logger = Logger.getLogger(CommonDaoImpl.class);

   @Autowired
   public CommonDaoImpl(DataSource dataSource) {
      super(dataSource);
   }

   @Transactional
   public boolean saveObject(Object object) {
      this.entityManager.persist(object);
      return true;
   }

   @Transactional
   public boolean updateObject(Object object) {
      this.entityManager.merge(object);
      return true;
   }

   public List<AdnetworkOperatorConfig> findAdnetworkOperatorConfig() {
      String queryStr = "select b.* from tb_adnetwork_operator_config b";
      Query query = this.entityManager.createNativeQuery(queryStr, AdnetworkOperatorConfig.class);
      return query.getResultList();
   }

   public List<Adnetworks> findAllEnableAdnetworks() {
      Query query = this.entityManager.createNamedQuery("Adnetworks.findAllEnableAdnetworks", Adnetworks.class);
      query.setParameter("status", true);
      return query.getResultList();
   }

   public Integer findNextAutoIncrementId(String tableName, String dbName) {
      String sqlString = "SELECT AUTO_INCREMENT FROM  INFORMATION_SCHEMA.TABLES  WHERE TABLE_SCHEMA =:dbName  AND   TABLE_NAME   = :tableName ;";
      Map<String, String> map = new HashMap<>();
      map.put("dbName", dbName);
      map.put("tableName", tableName);
      MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(map);
      return (Integer)this.queryForObject(sqlString, sqlParameterSource, Integer.class);
   }

   public SubscriberReg searchSubscriberByLcId(String lifeCycleId) {
      TypedQuery<SubscriberReg> query = this.entityManager.createNamedQuery("SubscriberReg.searchSubscriberByLcId", SubscriberReg.class);
      query.setParameter("lifeCycleId", lifeCycleId);
      return (SubscriberReg)query.getSingleResult();
   }

   public List<Operator> findAllOperator() {
      TypedQuery<Operator> query = this.entityManager.createNamedQuery("Operator.findAllOperator", Operator.class);
      return query.getResultList();
   }

   public List<Operator> findAllEnabledOperator() {
      TypedQuery<Operator> query = this.entityManager.createNamedQuery("Operator.findAllEnabledOperator", Operator.class);
      return query.getResultList();
   }

   public AdnetworkOperatorConfig findAdnetworkOperatorConfigById(Integer adnopconfigid) {
      TypedQuery<AdnetworkOperatorConfig> query = this.entityManager.createNamedQuery("AdnetworkOperatorConfig.findAdnetworkOperatorConfigById", AdnetworkOperatorConfig.class);
      query.setParameter("adnetworkOperatorConfigId", adnopconfigid);
      query.setMaxResults(1);
      return (AdnetworkOperatorConfig)query.getSingleResult();
   }

   @Transactional
   public boolean generateInappLiveReport(InappLiveReport liveReport) {
      String queryStr = "INSERT INTO tb_inapp_live_report( operator_id,report_date,adnetwork_campaign_id,pub_id,pin_request_count, pin_send_count,pin_validation_request_count, pin_validate_count,pin_validate_amount,status_check_request_count,send_conversion_count,send_conversion_amount,action_hours,service_id,last_update_time)\tVALUES (:operatorId,date(:reportDate),:adnetworkCampaignId,:pubId,:pinRequestCount,:pinSendCount,:pinValidationRequestCount,:pinValidateCount,:pinValidateAmount,:statusCheckRequestCount,:sendConversionCount, \t:sendConversionAmount,hour(:actionHours),:serviceId,now())  ON DUPLICATE KEY UPDATE  pin_request_count=pin_request_count+:pinRequestCount2, pin_send_count=pin_send_count+:pinSendCount2, pin_request_count=pin_request_count+:pinRequestCount2, pin_validation_request_count=pin_validation_request_count+:pinValidationRequestCount2, pin_validate_count=pin_validate_count+:pinValidateCount2, pin_validate_amount=pin_validate_amount+:pinValidateAmount2, status_check_request_count=status_check_request_count+:statusCheckRequestCount2, send_conversion_count=send_conversion_count+:sendConversionCount2, send_conversion_amount=send_conversion_amount+:sendConversionAmount2,last_update_time=now()";
      logger.debug("generateInappLiveReport::queryStr:  " + queryStr);
      Query query = this.entityManager.createNativeQuery(queryStr);
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
      return query.executeUpdate() > 0;
   }

   public List<InappLiveReport> findInappLiveReportAggReport(AggReport aggReport) {
      Map<String, Object> parameters = new HashMap<>();
      String queryStr = "select if(vwlive.operator_id is not null,vwlive.operator_id,0) as operatorId,if(vwlive.operator_name is not null,vwlive.operator_name,'Other') as operatorName,";
      if (aggReport.getReportType() != null && aggReport.getReportType().equalsIgnoreCase("MONTHLY")) {
         queryStr = queryStr + "concat(MONTHNAME(vwlive.report_date),'-',year(vwlive.report_date)) as reportDateStr,";
      } else {
         queryStr = queryStr + "vwlive.report_date as reportDateStr,";
      }

      queryStr = queryStr + " if(vwlive.network_name is null,'Other',vwlive.network_name) as networkName,"
      		+ "vwlive.adnetwork_campaign_id as adnetworkCampaignId,"
      		+ "vwlive.adnetworkid as adnetworkId,"
      		+ "vwlive.aggregator_id as aggregatorId,"
      		+ "vwlive.product_name as productName,"
      		+ "vws.service_id as serviceId,"
      		+ "vws.service_name as serviceName,"
      		+ "vwlive.advertiser_name as advertiserName, "
      		+ "COALESCE(sum(vwlive.pin_request_count),0) as pinRequestCount, "
      		+ "COALESCE(sum(vwlive.pin_send_count),0) as pinSendCount,"
      		+ "COALESCE(sum(vwlive.pin_validation_request_count),0) as pinValidationRequestCount,"
      		+ "COALESCE(sum(vwlive.pin_validate_count),0) as pinValidateCount,"
      		+ "COALESCE(sum(vwlive.pin_validate_amount),0) as pinValidateAmount,"
      		+ "COALESCE(sum(status_check_request_count),0) as statusCheckRequestCount,"
      		+ "COALESCE(sum(send_conversion_count),0) as sendConversionCount,"
      		+ "COALESCE(sum(send_conversion_amount),0) as sendConversionAmount"
      		+ " from vw_inapp_live_report_monthly_new vwlive  join vw_service vws on vwlive.service_id=vws.service_id  where  1=1  ";
      if (aggReport.getFromTime() != null) {
         queryStr = queryStr + " and date(report_date)>=date(:fromTime)";
         parameters.put("fromTime", aggReport.getFromTime());
      }

      if (aggReport.getToTime() != null) {
         queryStr = queryStr + " and date(report_date)<=date(:toTime)";
         parameters.put("toTime", aggReport.getToTime());
      }

      if (aggReport.getFromTime() == null && aggReport.getToTime() == null) {
         queryStr = queryStr + " and date(report_date)=date(now())";
      }

      if (aggReport.getAggregatorId() != null) {
         queryStr = queryStr + " and vwlive.aggregator_id=:aggregator_id";
         parameters.put("aggregator_id", aggReport.getAggregatorId());
      }

      if (aggReport.getAdnetworkId() != null && aggReport.getAdnetworkId() > 0) {
         queryStr = queryStr + " and vwlive.adnetworkid=:adnetworkid";
         parameters.put("adnetworkid", aggReport.getAdnetworkId());
      }

      if (aggReport.getOpid() != null && aggReport.getOpid() > 0) {
         queryStr = queryStr + " and vwlive.operator_id=:operator_id";
         parameters.put("operator_id", aggReport.getOpid());
      }

      if (aggReport.getProductId() != null) {
         queryStr = queryStr + " and vwlive.product_id=:product_id";
         parameters.put("product_id", aggReport.getProductId());
      }
      
      if(aggReport.getAdvertiserid()!=null) {
    	  queryStr = queryStr + " and vwlive.advertiser_id=:advertiser_id";
          parameters.put("advertiser_id", aggReport.getAdvertiserid());
    	  
      }

      queryStr = queryStr + " group by 1,3,5,9 order by 1 asc,3 asc";
      logger.info("query str: " + queryStr + " ,parameters:: " + parameters);
      List<InappLiveReport> list = query(queryStr, parameters, new BeanPropertyRowMapper<InappLiveReport>(InappLiveReport.class));
      return list;
   }

   public InappLiveReport getInappLiveReport() {
      Map<String, Object> parameters = new HashMap<>();
      String queryStr = "select * from tb_inapp_live_report ORDER BY id desc limit 1";
      InappLiveReport inappLiveReport = queryForObject(queryStr, parameters, new BeanPropertyRowMapper<InappLiveReport>(InappLiveReport.class));
      return inappLiveReport;
   }

   public SubscriberReg searchSubscriber(String msisdn) {
      Query query = this.entityManager.createNamedQuery("SubscriberReg.findSubscriberRegByMsisdn", SubscriberReg.class);
      query.setParameter("msisdn", msisdn);
      SubscriberReg subscriberReg = (SubscriberReg)query.getSingleResult();
      return subscriberReg;
   }

   public List<VWAdnetworkOperatorConfig> findAllAdnConfig() {
      TypedQuery<VWAdnetworkOperatorConfig> query = this.entityManager.createNamedQuery("VWAdnetworkOperatorConfig.findAllAdnConfig", VWAdnetworkOperatorConfig.class);
      return query.getResultList();
   }

   public List<VWCampaignDetail> findEnableVWServiceCampaignDetail() {
      TypedQuery<VWCampaignDetail> query = this.entityManager.createNamedQuery("VWServiceCampaignDetail.findEnableVWServiceCampaignDetail", VWCampaignDetail.class);
      query.setParameter("campaignDetailsStatus", true);
      query.setParameter("serviceStatus", true);
      return query.getResultList();
   }

   @Transactional
   public List<Object> getDataList(Query query) {
      return query.getResultList();
   }

   @Transactional
   public boolean checkExistingRecord(Query query) {
      Object existingRecord = null;

      try {
         existingRecord = query.getSingleResult();
      } catch (NoResultException var4) {
         logger.error("existing config error : " + var4);
      }

      return existingRecord != null;
   }

   public Object getSingleRecord(Query query) {
      Object record = null;

      try {
         record = query.getSingleResult();
      } catch (NoResultException var4) {
         logger.error("single record error : " + var4);
      }

      return record;
   }
   
	@Override
	public List<InAppAdverterReport> findInappAdvertiserReport(AggReport aggReport){
	
		
		Map<String, Object> parameters = new HashMap<String, Object>();
				
		String  queryStr="select vwadr.id AS id,if(vwadr.cmpid is not null,vwadr.cmpid,0) as cmpId,";
		if(aggReport.getReportType()!=null&&aggReport.getReportType().equalsIgnoreCase(MConstants.MONTHLY_REPORT_TYPE)){	     
		queryStr+=  "concat(MONTHNAME(vwadr.create_time),'-',year(vwadr.create_time)) as createDate,";
		}else{
			queryStr+=  "vwadr.create_time as createDate,";
		}
		queryStr+=" if(vwadr.advertiser_name is null,'Other',vwadr.advertiser_name) as advertiserName,"
				 +" COALESCE((vwadr.service_id),0) as serviceId,"
				 +" COALESCE((vwadr.advertiser_id),0) as advertiserId,"
			     +" if(vwadr.service_name is null,'Other',vwadr.service_name) as serviceName,"
			     + "vwadr.action_type as actionType,"
			     + "vwadr.advertiser_api_request as advertiserApiRequest,"
			     + "vwadr.advertiser_api_response as advertiserApiResponse"
			     + " from vw_inapp_advertiser_report vwadr"
			     + " where  1=1  ";
		
			if(aggReport.getFromTime()!=null){
				 queryStr+= " and date(create_time)>=date(:fromTime)";
				 parameters.put("fromTime",aggReport.getFromTime());
			}//date(report_date)=date(now())
		
			if(aggReport.getToTime()!=null){
				 queryStr+= " and date(create_time)<=date(:toTime)";
				 parameters.put("toTime",aggReport.getToTime());
			}
			if(aggReport.getFromTime()==null&&aggReport.getToTime()==null){
				queryStr+= " and date(create_time)=date(now())";
			}
			
			if(aggReport.getCmpid()!=null){
				queryStr+= " and vwadr.cmpid=:cmpid";
				parameters.put("cmpid",aggReport.getCmpid());
			}
			
			if(aggReport.getAdvertiserid()!=null){
				queryStr+= " and vwadr.advertiser_id=:advertiser_id";
				parameters.put("advertiser_id",aggReport.getAdvertiserid());
			}
			if(aggReport.getActionType()== null || aggReport.getActionType().equalsIgnoreCase("none")){
				
			}else {
				queryStr+= " and vwadr.action_type=:action_type";
				parameters.put("action_type",aggReport.getActionType());
			}
			
			
		   queryStr+= " order by 1 desc limit "+aggReport.getPageNo()+", "+MConstants.PAGE_SIZE;
				   
				   logger.info("query str: "+queryStr+" ,parameters:: "+parameters);
				 List<InAppAdverterReport> list = query(queryStr, parameters,new BeanPropertyRowMapper<InAppAdverterReport>(InAppAdverterReport.class));
				 return list;
			
	}
	
	@Override
	public long findInappAdvertiserReportCount(AggReport aggReport) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		String  queryStr="select count(vwadr.cmpid)  from vw_inapp_advertiser_report vwadr where 1=1";
			
		if(aggReport.getFromTime()!=null){
			 queryStr+= " and date(create_time)>=date(:fromTime)";
			 parameters.put("fromTime",aggReport.getFromTime());
		}//date(report_date)=date(now())
	
		if(aggReport.getToTime()!=null){
			 queryStr+= " and date(create_time)<=date(:toTime)";
			 parameters.put("toTime",aggReport.getToTime());
		}
		if(aggReport.getFromTime()==null&&aggReport.getToTime()==null){
			queryStr+= " and date(create_time)=date(now())";
		}
		
		if(aggReport.getCmpid()!=null){
			queryStr+= " and vwadr.cmpid=:cmpid";
			parameters.put("cmpid",aggReport.getCmpid());
		}
		
		if(aggReport.getAdvertiserid()!=null){
			queryStr+= " and vwadr.advertiser_id=:advertiser_id";
			parameters.put("advertiser_id",aggReport.getAdvertiserid());
		}
		if(aggReport.getActionType()== null || aggReport.getActionType().equalsIgnoreCase("none")){
			
		}else {
			queryStr+= " and vwadr.action_type=:action_type";
			parameters.put("action_type",aggReport.getActionType());
		}
		   logger.info("query str: "+queryStr+" ,parameters:: "+parameters);
			 long listsize = queryForObject(queryStr, parameters, Long.class);
			 return listsize;
	}
	
	@Override
	public UniqueCount findInappUniqueCount(InappLiveReport inappLiveReport) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		UniqueCount uniqueCount =new UniqueCount();
		String pinRequestQueryStr ="";
		String pinSendQueryStr ="";
		String pinvalidateQueryStr ="";
		Integer requestcount  =0;
		Integer pinSendcount  =0; 
		Integer pinValidatecount  =0;	
		try {
			pinRequestQueryStr = "SELECT COALESCE(COUNT(DISTINCT(msisdn)),0) FROM tb_inapp_process_request "
				+ "where cmpid = "+inappLiveReport.getAdnetworkCampaignId()+" and service_id = "+inappLiveReport.getServiceId()+" and create_time like \"%"+inappLiveReport.getReportDateStr()+"%\" GROUP BY cmpid,service_id,date(create_time) order by date(create_time)";
			pinSendQueryStr="SELECT COALESCE(COUNT(DISTINCT(msisdn)),0) FROM tb_inapp_process_request "
					+ "where cmpid = "+inappLiveReport.getAdnetworkCampaignId()+" and service_id = "+inappLiveReport.getServiceId()+" and create_time like \"%"+inappLiveReport.getReportDateStr()+"%\" and `action` ='SEND_PIN' and (advertiser_api_response like \"%SUCCESS%\" OR advertiser_api_response = \"1\") GROUP BY cmpid,service_id,date(create_time) order by date(create_time)";
			pinvalidateQueryStr="SELECT COALESCE(COUNT(DISTINCT(msisdn)),0) FROM tb_inapp_process_request "
					+ "where cmpid = "+inappLiveReport.getAdnetworkCampaignId()+" and service_id = "+inappLiveReport.getServiceId()+" and create_time like \"%"+inappLiveReport.getReportDateStr()+"%\" and `action` ='PIN_VALIDATION' and (advertiser_api_response like \"%SUCCESS%\" OR advertiser_api_response = \"1\") GROUP BY cmpid,service_id,date(create_time) order by date(create_time)";
		logger.info("pinRequestQueryStr: "+pinRequestQueryStr);	
		logger.info("pinSendQueryStr: "+pinSendQueryStr);
		logger.info("pinvalidateQueryStr: "+pinvalidateQueryStr);
		try {
		  requestcount  = queryForObject(pinRequestQueryStr, parameters,Integer.class);
		}catch(Exception e) {
			logger.info("err"+e);
		}
		try {
			logger.info("requestcount: "+requestcount);
//			pinSendcount  =25;
		  pinSendcount  = queryForObject(pinSendQueryStr, parameters,Integer.class);
		}catch(Exception e) {			logger.info("err"+e);}
		try {
			logger.info("pinSendcount: "+pinSendcount);
		  pinValidatecount  = queryForObject(pinvalidateQueryStr, parameters,Integer.class);
		}catch(Exception e) {			logger.info("err"+e);}
		 logger.info("pinValidatecount: "+pinValidatecount);
		 uniqueCount.setCmpId(inappLiveReport.getAdnetworkCampaignId());
		 uniqueCount.setCreatetime(inappLiveReport.getReportDateStr());
		 uniqueCount.setServiceName(inappLiveReport.getServiceName());
		 uniqueCount.setAdvertiserName(inappLiveReport.getAdvertiserName());
		 uniqueCount.setUniquePinRequestCount(requestcount);
		 uniqueCount.setUniquePinSendCount(pinSendcount);
		 uniqueCount.setUniquePinValidateCount(pinValidatecount);
		}catch(Exception e) {
			uniqueCount.setCmpId(inappLiveReport.getAdnetworkCampaignId());
			 uniqueCount.setAdvertiserName(inappLiveReport.getAdvertiserName());
			 uniqueCount.setCreatetime(inappLiveReport.getReportDateStr());
			 uniqueCount.setServiceName(inappLiveReport.getServiceName());
			 uniqueCount.setCreatetime(inappLiveReport.getReportDateStr());
			logger.info("UniqueCount Error:  "+e);
		}
		
		return uniqueCount;
	}

	
}