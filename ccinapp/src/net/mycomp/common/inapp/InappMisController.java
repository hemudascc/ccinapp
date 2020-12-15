
package net.mycomp.common.inapp;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;














import net.jpa.repository.JPATrafficRouting;
import net.jpa.repository.JPAVWTrafficRouting;
import net.mycomp.common.service.BlockSeriesRedisCacheService;
import net.mycomp.common.service.CommonService;
import net.mycomp.common.service.IDaoService;
import net.mycomp.common.service.RedisCacheService;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.Service;
import net.persist.bean.TrafficRouting;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.persist.bean.VWTrafficRouting;
import net.process.bean.AggReport;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Controller
@RequestMapping(value = "inapp")
public class InappMisController {

	private static  Logger logger = Logger.getLogger(InappMisController.class);

	@Autowired
	private IDaoService daoService;

	@Autowired
	private JPATrafficRouting jpaTrafficRouting;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BlockSeriesRedisCacheService blockSeriesRedisCacheService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private JPAVWTrafficRouting jpaVWTrafficRouting;
	
	@InitBinder
	public void binder(WebDataBinder binder) {binder.registerCustomEditor(Timestamp.class,
	    new PropertyEditorSupport() {
	        public void setAsText(String value) {
	            try {
	                Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(value);
	                setValue(new Timestamp(parsedDate.getTime()));
	            } catch (ParseException e) {
	                setValue(null);
	            }
	            
	        }
	    });
	}
	

	
	
	
	@RequestMapping("/aggstats")	
	public ModelAndView aggReport(@ModelAttribute(value="AggReport") AggReport aggReport,BindingResult result) {
		
		ModelAndView modelAndView=new ModelAndView("inapp/agg_report");
		
		//modelAndView.addObject("mapAggregator",MData.mapIdToAggregator);
		modelAndView.addObject("mapAggregator", MData.mapIdToAggregator.entrySet().stream()
                .filter(map -> map.getValue().getType().equalsIgnoreCase(InappConstant.INAPP))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		
		
		modelAndView.addObject("mapOperator",MData.mapIdToOperator);
		modelAndView.addObject("mapProduct",MData.mapIdToProduct);
		modelAndView.addObject("productId",aggReport.getProductId());
		
		modelAndView.addObject("listAggregator",MData.mapIdToAggregator.values().stream()
				.collect(Collectors.toList()));
		
		if(aggReport.getAggregatorId()!=null){
			modelAndView.addObject("operatorList", MData.mapIdToOperator.values().stream()
			.filter(p -> p.getAggregatorId().intValue()==aggReport.getAggregatorId())
			.collect(Collectors.toList()));
		}
		
//		if(aggReport.getOpid()!=null){
//			Map<Integer,Product> mapProduct=new HashMap<Integer,Product>();
//			
//			for(Service service:MData.mapServiceIdToService.values()){
//				if(aggReport.getOpid().intValue()==service.getOpId()){
//				  mapProduct.put(service.getProductId(),MData.mapIdToProduct.get(service.getProductId()));
//				}
//			}
//			modelAndView.addObject("productList", mapProduct.values().stream()
//					.collect(Collectors.toList()));
//		}
		
		modelAndView.addObject("adnetworksList", MData.mapAdnetworks.values().stream()
				.collect(Collectors.toList()));
		
		List<InappLiveReport> list = daoService.findInappLiveReportAggReport(aggReport);
		modelAndView.addObject("report",list);
		Map<Integer,List<InappLiveReport>> map=null;
	   if(list!=null){
		 map=list.stream()
	        .collect(Collectors.groupingBy(p->p.getOperatorId(),Collectors.toList()
	        ));
	   }
		modelAndView.addObject("reportMap",map);
		//Map<Integer,Integer> mapActiveBase=daoService.findSubscriberActiveBase(aggReport);
		modelAndView.addObject("mapActiveBase",null);
		return modelAndView;
	}
	
	
	@RequestMapping("/block")
	@ResponseBody
	public String isBlock(@RequestParam("key")String key){
		//List<String> keys=new ArrayList<String>();
		  
		 List<String> keys=Arrays.asList(new String[]{
				  MUtility.find7DigitMobileNo(key),
				  MUtility.find11DigitMobileNo(key)					
				  });
		boolean isBlock=false;
		boolean dctBlock=false;
		String detail=null;
		try {
			isBlock = blockSeriesRedisCacheService.isBlockSeries(keys);
			 detail=blockSeriesRedisCacheService.getBlockSeriesSizeAndKeyValue(keys);
			 dctBlock=redisCacheService.getObjectCacheValue(MConstants.DCT_BLOCK_PREFIX+key)!=null?true:false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "isBlock:: "+keys+" ,"+isBlock+" <BR> Block series detail:: "+detail+"<br> dct block: "+dctBlock;
		
	}
	

	@RequestMapping("adnoconfig")
	public ModelAndView findAdnoConfig(){
		ModelAndView modelAndView=new ModelAndView("adnoconfig");
		List<VWAdnetworkOperatorConfig> list=daoService.findAllAdnConfig();
		modelAndView.addObject("list",list);
		modelAndView.addObject("lastreloadtime",commonService.lastreloadConfigTime);
		return modelAndView;
	}
	
	@RequestMapping("updateadnopconfig")
	public ModelAndView updateadnopconfig(HttpServletRequest request,@RequestParam(value="adnopconfigid")
	 Integer adnopconfigid,@RequestParam(value="skipno",defaultValue="0")Integer skipno){
		AdnetworkOperatorConfig adnetworkOperatorConfig=daoService.
				findAdnetworkOperatorConfigById(adnopconfigid);
		adnetworkOperatorConfig.setSkipNumber(skipno);
		daoService.updateObject(adnetworkOperatorConfig);
		logger.info("updateadnopconfig:: query string "+request.getQueryString());		
		ModelAndView modelAndView=new ModelAndView("redirect:/cnt/inapp/adnoconfig");
		return modelAndView;
	}
	
	@RequestMapping("/reload")
	public ModelAndView reload(){
		commonService.loadConfiguration();
		HttpURLConnectionUtil httpURLConnectionUtil=new HttpURLConnectionUtil();
		httpURLConnectionUtil.invokeGetURL("http://192.241.253.234/inapp/cnt/inapp/reloadapp");
		httpURLConnectionUtil.invokeGetURL("http://192.241.253.234/ccinapp/cnt/inapp/reloadapp");
		
		ModelAndView modelAndView=new ModelAndView("redirect:/cnt/inapp/adnoconfig");
		return modelAndView;
	}
	
	
	@RequestMapping("/reloadapp")
	@ResponseBody
	public String reloadAll(){
		commonService.loadConfiguration();
		return "ok";
	}

	
	
	@ExceptionHandler
	@ResponseBody
	public String error(Exception ex){
		logger.error("error:: ",ex);
		return ex.getMessage();
	}
	
	
	@RequestMapping("/routing")	
	public ModelAndView routing(@ModelAttribute(value="AggReport") AggReport aggReport,BindingResult result) {
		
		ModelAndView modelAndView=new ModelAndView("routing");
	
		modelAndView.addObject("mapCamapignIdToVWCampaignDetail", MData.mapCamapignIdToVWCampaignDetail);
		modelAndView.addObject("mapServiceIdToService", MData.mapServiceIdToService);
		
		List<VWTrafficRouting> listVWTrafficRouting = jpaVWTrafficRouting.findAll();		
		modelAndView.addObject("mapcampaignIdIdToVWTrafficRouting",listVWTrafficRouting
				.stream().collect(Collectors.
						groupingBy(VWTrafficRouting::getCampaignId, 
								Collectors.mapping(a->a, Collectors.toList()))));		
		return modelAndView;
	}
	
	@RequestMapping("find/trafficrouting")
	@ResponseBody
	public String findVWRoutingById(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		VWTrafficRouting vwTrafficRouting=jpaVWTrafficRouting.findVWTrafficRoutingByTraffingId(MUtility.toInt(request.getParameter("id")
				, 0));
		return JsonMapper.getObjectToJson(vwTrafficRouting);
	}
	
	@RequestMapping("update/trafficrouting")
	@ResponseBody
	public Boolean updateTrafficRouting(HttpServletRequest request,HttpServletResponse response) {
		TrafficRouting trafficRoouting=new TrafficRouting();
		Integer trafiicRoutingId=MUtility.toInt(request.getParameter("trafiicroutingid"), 0);	
		
		Integer campaignId=MUtility.toInt(request.getParameter("campaignid"), 0);
		Integer serviceId=MUtility.toInt(request.getParameter("serviceid"), 0);
		Integer trafficpercentage=MUtility.toInt(request.getParameter("trafficpercentage"), 0);
		trafficRoouting=jpaTrafficRouting.findTrafficRoutingByTraffingId(trafiicRoutingId);
		if(trafficRoouting==null){
			trafficRoouting=new TrafficRouting();
		}
		
		trafficRoouting.setCampaignId(campaignId);
		trafficRoouting.setServiceId(serviceId);
		trafficRoouting.setPercentageOfTraffic(trafficpercentage);
		trafficRoouting.setTrafiicRoutingStatus(true);
		jpaTrafficRouting.save(trafficRoouting);
		return true;
	}
	
	@RequestMapping("remove/trafficrouting")
	@ResponseBody
	public Boolean removeTrafficRouting(HttpServletRequest request,HttpServletResponse response) {
		Integer trafiicRoutingId=MUtility.toInt(request.getParameter("trafiicroutingid"), 0);		
		TrafficRouting trafficRoouting=jpaTrafficRouting.findTrafficRoutingByTraffingId(trafiicRoutingId);
		jpaTrafficRouting.delete(trafficRoouting);
		return true;
	}
}
