package net.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Advertiser;
import net.persist.bean.Aggregator;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.Service;
import net.persist.bean.VWCampaignDetail;
import net.persist.bean.VWTrafficRouting;

public interface MData {

	 static final Logger logger = Logger.getLogger(MData.class);

	public static final Map<Integer, Map<Integer, AdnetworkOperatorConfig>> mapAdnetworkOpConfig=new HashMap<Integer, Map<Integer, AdnetworkOperatorConfig>>();
	public static final Map<Integer,VWCampaignDetail> mapCamapignIdToVWCampaignDetail=new HashMap<Integer,VWCampaignDetail>();
	public static final Map<Integer,List<VWCampaignDetail>> mapSmartCamapignIdToVWCampaignDetail=new HashMap<Integer,List<VWCampaignDetail>>();
	public static Map<Integer, Adnetworks> mapAdnetworks=new HashMap<Integer, Adnetworks>();   
	public static Map<Integer, Service> mapServiceIdToService=new HashMap<Integer, Service>();	
	public static Map<Integer,Aggregator> mapIdToAggregator=new HashMap<Integer, Aggregator>();
	public static Map<Integer, Country> mapIdToCountry=new HashMap<Integer, Country>();
	public static Map<Integer, Operator> mapIdToOperator=new HashMap<Integer, Operator>();
	public static Map<Integer, Product> mapIdToProduct=new HashMap<Integer, Product>();
	public static Map<Integer, Advertiser> mapIdToAdvertiser=new HashMap<Integer, Advertiser>();
	
	public static Map<Integer, List<VWTrafficRouting>> mapcampaignIdIdToVWTrafficRouting=new HashMap<Integer, List<VWTrafficRouting>>();
	

}
