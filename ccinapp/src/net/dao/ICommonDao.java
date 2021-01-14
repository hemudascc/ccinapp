package net.dao;

import java.util.List;

import net.mycomp.common.inapp.InappAutomatedProcessRequest;
import net.mycomp.common.inapp.InappLiveReport;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Operator;
import net.persist.bean.SubscriberReg;
import net.persist.bean.TrafficRouting;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.persist.bean.VWCampaignDetail;
import net.process.bean.AggReport;
import net.persist.bean.Country;
import net.persist.bean.Product;
import net.persist.bean.Aggregator;
import net.persist.bean.Service;
import net.persist.bean.CampaignDetails;
import net.persist.bean.AdnetworkOperatorConfig;

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
		
	 /* In App Automated methods*/
	 
	 public Object findAllCountry();
	 public Object findAllProduct();
	 public Object findAllAggregator();
	 public Object findAllSerivce();
	 public Object findAllCampaignDetails();
	 public Object saveCampaignDetails(CampaignDetails object);
	 public Object saveCountry(Country object);
	 public Object saveProduct(Product object);
	 public Object saveAggregator(Aggregator object);
	 public Object saveSerivce(Service object);
	 public Object saveOperator(Operator object);
	 public Object saveAdnetworkOperatorConfig(AdnetworkOperatorConfig object);
	 public Object advertiserConfig(InappAutomatedProcessRequest inappautomatedprocessrequest);
	 public Object saveTrafficRouting(TrafficRouting trafficrouting);
}
