package net.mycomp.common.service;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import net.dao.ICommonDao;
import net.mycomp.common.inapp.InappLiveReport;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Operator;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.persist.bean.VWCampaignDetail;
import net.process.bean.AggReport;

@Service("daoService")
public class DaoServiceImpl implements IDaoService {

	@Autowired
	@Qualifier("commonDao")
	private ICommonDao commonDao;

	private static final Logger logger = Logger.getLogger(DaoServiceImpl.class);

	@Override
	public boolean saveObject(Object object) {
		try {
			return commonDao.saveObject(object);
		} catch (Exception ex) {
			logger.error("saveObject:: exception:: "+object +" , ",ex);
		}
		return false;
	}

	@Override
	public boolean updateObject(Object object) {
		try {
			return commonDao.updateObject(object);
		} catch (Exception ex) {
			logger.error("updateObject:: "+object , ex);
		}
		return false;
	}

	
	@Override
	public SubscriberReg searchSubscriber(String msisdn) {
		try {
			return commonDao.searchSubscriber(msisdn);
		} catch (Exception ex) {
			logger.info("searchSubscriber:: not found : " + ex+", msisdn:: "+msisdn);
		}
		return null;
	}

	/*
	 * @Override public ProSubsPackResp callSubscribePackageProcedure(
	 * AdNetworkRequestBean adNetworkRequestBean, SearchCampaign searchCampaign)
	 * { try { return
	 * commonDao.callSubscribePackageProcedure(adNetworkRequestBean,
	 * searchCampaign); } catch (Exception ex) { logger.error(
	 * "callSubscribePackageProcedure:: exception : "+ex); } return null; }
	 */

	

	public List<AdnetworkOperatorConfig> findAdnetworkOperatorConfig() {
		try {
			return commonDao.findAdnetworkOperatorConfig();
		} catch (Exception ex) {
			logger.error("findAdnetworkOperatorConfig:: exception : " , ex);
		}
		return null;
	}

	

	

	
	
	
	

	@Override
	public List<Adnetworks> findAllEnableAdnetworks() {
		try {
			return commonDao.findAllEnableAdnetworks();
		} catch (Exception ex) {
			logger.error("findAllEnableAdnetworks:: exception : " + ex);
		}
		return null;
	}

	@Override
	public Integer findNextAutoIncrementId(String tableName, String dbName) {
		try {
			return commonDao.findNextAutoIncrementId(tableName,dbName);
		} catch (Exception ex) {
			logger.error("findAllEnableAdnetworks:: exception : " + ex);
		}
		return null;
	}

	
	
	
	@Override
	public List<Operator> findAllOperator() {
		try {
			return commonDao.findAllOperator();
		} catch (Exception ex) {
			logger.error("findAllOperator:: exception : " , ex);
		}
		return null;
	}

	
	@Override
	public List<VWCampaignDetail> findEnableVWServiceCampaignDetail() {
		try {
			return commonDao.findEnableVWServiceCampaignDetail();
		} catch (Exception ex) {
			logger.error("findEnableVWServiceCampaignDetail:: exception : " , ex);
		}
		return null;
	}

	@Override
	public List<Operator> findAllEnabledOperator() {
		try {
			return commonDao.findAllEnabledOperator();
		} catch (Exception ex) {
			logger.error("findAllEnabledOperator:: exception : " , ex);
		}
		return null;
	}

	
	

	@Override
	public AdnetworkOperatorConfig findAdnetworkOperatorConfigById(
			Integer adnopconfigid) {
		try {
			return commonDao.findAdnetworkOperatorConfigById(adnopconfigid);
		} catch (Exception ex) {
			logger.error("findAdnetworkOperatorConfigById:: not found : " ,ex);
		}
		return null;
	}

	

	@Override
	public boolean generateInappLiveReport(InappLiveReport liveReport) {
		try {
			return commonDao.generateInappLiveReport(liveReport);
		} catch (Exception ex) {
			logger.error("generateInappLiveReport::  : " ,ex);
		}
		return false;
	}

	@Override
	public List<InappLiveReport> findInappLiveReportAggReport(
			AggReport aggReport) {
		try {
			return commonDao.findInappLiveReportAggReport(aggReport);
		} catch (Exception ex) {
			logger.error("findInappLiveReportAggReport::  : " ,ex);
		}
		return null;
	}

	@Override
	public List<VWAdnetworkOperatorConfig> findAllAdnConfig() {
		try {
			return commonDao.findAllAdnConfig();
		} catch (Exception ex) {
			logger.error("findInappLiveReportAggReport::  : " ,ex);
		}
		return null;
	}

	@Override
	public List<Object> getDataList(Query query){
		try {
			return commonDao.getDataList(query);
		} catch (Exception ex) {
			logger.error("checkExistingRecord:: "+ ex);
		}
		return null;
	}
	
	@Override
	public boolean checkExistingRecord(Query query) {
		try {
			return commonDao.checkExistingRecord(query);
		} catch (Exception ex) {
			logger.error("checkExistingRecord:: "+ ex);
		}
		return false;
	} 

	@Override
	public Object getSingleRecord(Query query) {
		try {
		return commonDao.getSingleRecord(query);
		}catch(Exception ex) {
			logger.info("Error in getSingleRecord");
		}
		return null;
	}
	
}
