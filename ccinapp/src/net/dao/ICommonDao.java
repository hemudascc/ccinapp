package net.dao;

import java.util.List;
import net.mycomp.common.inapp.InappLiveReport;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Operator;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.persist.bean.VWCampaignDetail;
import net.process.bean.AggReport;

public interface ICommonDao {

	public boolean saveObject(Object object);
	public boolean updateObject(Object object);
	public SubscriberReg searchSubscriber(String msisdn);	
	public List<AdnetworkOperatorConfig> findAdnetworkOperatorConfig();
	public List<Adnetworks> findAllEnableAdnetworks();
	public Integer findNextAutoIncrementId(String tableName, String dbName);
	
	public SubscriberReg searchSubscriberByLcId(String lifeCycleId); 
	public List<Operator> findAllOperator();
	public List<Operator> findAllEnabledOperator();
	//public List<VWServiceCampaignDetail> findEnableVWServiceCampaignDetail(); 
	
	 public AdnetworkOperatorConfig findAdnetworkOperatorConfigById(Integer adnopconfigid);
	 public boolean generateInappLiveReport(InappLiveReport liveReport);
	 public List<InappLiveReport> findInappLiveReportAggReport(
				AggReport aggReport);
	 public List<VWAdnetworkOperatorConfig> findAllAdnConfig();
	 public List<VWCampaignDetail> findEnableVWServiceCampaignDetail();
				
}
