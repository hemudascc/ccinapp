package net.mycomp.common.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.jpa.repository.JPAAggregator;
import net.jpa.repository.JPACountry;
import net.jpa.repository.JPAProduct;
import net.jpa.repository.JPAService;
import net.jpa.repository.JPAVWCampaignDetail;
import net.jpa.repository.JPAVWTrafficRouting;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Adnetworks;
import net.persist.bean.Aggregator;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.VWCampaignDetail;
import net.persist.bean.VWTrafficRouting;
import net.util.HttpURLConnectionUtil;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Service("commonService" )
public class CommonService {

	private static final Logger logger = Logger.getLogger(CommonService.class);

	
	public static Timestamp lastreloadConfigTime;

	public static Map<Integer,Operator> mapOperator=new HashMap<Integer,Operator>();
	public static List<Operator> listOperator;
	protected HttpURLConnectionUtil httpURLConnectionUtil;

	@Autowired
	private IDaoService daoService;

	@Autowired
	private JPAService jpaService;
	
	@Autowired
	private JPAAggregator jpaAggregator;
	
	@Autowired
	private JPACountry jpaCountry;
	
	@Autowired
	private JPAProduct jpaProduct;
	
	@Autowired
	private JPAVWTrafficRouting jpaVWTrafficRouting;
  
	@Autowired
	private JPAVWCampaignDetail jpaVWCampaignDetail;

	@PostConstruct
	public void init() {
		httpURLConnectionUtil = new HttpURLConnectionUtil();	
				loadConfiguration();
	}

	public void loadConfiguration() {

		List<net.persist.bean.Service> listService =jpaService.findAll();
		MData.mapServiceIdToService.putAll(listService.stream().collect(
				Collectors.toMap(p -> p.getServiceId(), p -> p)));
		
		List<Adnetworks> listAdnetworks = daoService.findAllEnableAdnetworks();
		Map<Integer, Adnetworks> mapAdnetworks = new HashMap<Integer, Adnetworks>();
		for (Adnetworks adnetworks : listAdnetworks) {
			mapAdnetworks.put(adnetworks.getAdNetworkId(), adnetworks);
		}
		MData.mapAdnetworks.clear();
		MData.mapAdnetworks.putAll(mapAdnetworks);

		List<AdnetworkOperatorConfig> adnOpConfigList = daoService.findAdnetworkOperatorConfig();
		logger.info("loadConfiguration:::::::::::::::AdnetworkOperatorList:: " + adnOpConfigList);
		
		adnOpConfigList.forEach(a->{
			int gcd=MUtility.gcd(a.getSkipNumber(), 100);
			a.atomicActSkipNumber.set(0, a.getSkipNumber()/gcd);
			a.atomicActSkipNumber.set(1, 100/gcd);
			a.atomicActSkipNumber.set(2,1);});
		
		
		MData.mapAdnetworkOpConfig.clear();
		MData.mapAdnetworkOpConfig.putAll(adnOpConfigList.stream()
				.collect(Collectors.groupingBy(AdnetworkOperatorConfig::getOperatorId,
						Collectors.toMap(AdnetworkOperatorConfig::getAdNetworkId, a ->a))));
		
		List<VWCampaignDetail> listVWServiceCampaignDetail = 
				jpaVWCampaignDetail.findEnableVWCampaignDetail(true);
		
		MData.mapCamapignIdToVWCampaignDetail.clear();
		MData.mapCamapignIdToVWCampaignDetail.putAll(listVWServiceCampaignDetail
				.stream().collect(Collectors.toMap(VWCampaignDetail::getCampaignId, 
				Function.identity())));
		
//		List<VWServiceCampaignDetail> listSmartVWServiceCampaignDetail=daoService.
//				findEnableSmartVWServiceCampaignDetail();
//		if(listSmartVWServiceCampaignDetail!=null){
//			MData.mapSmartCamapignIdToVWServiceCampaignDetail.clear();
//			MData.mapSmartCamapignIdToVWServiceCampaignDetail.putAll(listSmartVWServiceCampaignDetail
//					.stream().collect(Collectors.
//							groupingBy(VWServiceCampaignDetail::getAdNetworkId, 
//									Collectors.mapping(a->a, Collectors.toList()))));
//		}
			
	
	
		List<Aggregator> listAggregator=jpaAggregator.findAll();
		MData.mapIdToAggregator.putAll(listAggregator.stream().collect(
				Collectors.toMap(p -> p.getId(), p -> p)));
		
		List<Country> listCountry=jpaCountry.findAll();
		MData.mapIdToCountry.putAll(listCountry.stream().collect(
				Collectors.toMap(p -> p.getId(), p -> p)));
		
		List<Operator> listOperator=daoService.findAllEnabledOperator();
		MData.mapIdToOperator.putAll(listOperator.stream().collect(
				Collectors.toMap(p -> p.getOperatorId(), p -> p)));		
		
		List<Product> listProduct=jpaProduct.findAll();
		MData.mapIdToProduct.putAll(listProduct.stream().collect(
				Collectors.toMap(p -> p.getId(), p -> p)));
		
		List<VWTrafficRouting> listVWTrafficRouting=jpaVWTrafficRouting.findAll();
		
		
		MData.mapcampaignIdIdToVWTrafficRouting.putAll(listVWTrafficRouting
		.stream().collect(Collectors.
				groupingBy(VWTrafficRouting::getCampaignId, 
						Collectors.mapping(a->a, Collectors.toList()))));
		
		loadAdnetworkTypeConfig();
		
	}

	private void loadAdnetworkTypeConfig() {

	}
	
}
