package net.mycomp.common.inapp;

import java.util.ArrayList;
import java.util.List;

import net.jpa.repository.JPABlockSeries;
import net.mycomp.common.service.BlockSeriesRedisCacheService;
import net.mycomp.common.service.RedisCacheService;
import net.persist.bean.BlockSeries;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractInappOperatorServiceApi implements IInappOperatorServiceApi{

	private static final Logger logger = Logger.getLogger(AbstractInappOperatorServiceApi.class);
	
	
	@Autowired
	private BlockSeriesRedisCacheService blockSeriesRedisCacheService;
	
	@Autowired
	private JPABlockSeries jpaBlockSeries;
	
	@Override
	public boolean sendPin(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		logger.info("sendPin not supported");
		return false;
	}

	@Override
	public boolean validatePin(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		logger.info("validatePin not supported");
		return false;
	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		logger.info("statusCheck not supported");
		return false;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		logger.info("portalUrl not supported");
		return null;
	}

	@Override
	public boolean checkBlocking(InappProcessRequest inappProcessRequest) {	
		try{
		logger.info("checkBlocking not supported");
	    List<String> listMsisdn=new ArrayList<String>();
	    listMsisdn.add(inappProcessRequest
				.getServiceId()+"-"+inappProcessRequest.getMsisdn());
	    
		return blockSeriesRedisCacheService.isBlockSeries(listMsisdn);
		}catch(Exception ex){
			logger.error("checkBlocking  ",ex);
		}
		return false;
	}
	
	public boolean dct(InappProcessRequest inappProcessRequest){
		return false;
	}
	
	@Override
	public boolean addToBlock(InappProcessRequest inappProcessRequest){
		try{
			BlockSeries blockSeries=new BlockSeries(true);
			blockSeries.setServiceId(inappProcessRequest.getServiceId());
			blockSeries.setSeriesNo(inappProcessRequest.getMsisdn());
			jpaBlockSeries.save(blockSeries);
			blockSeriesRedisCacheService.putBlockSeriesValue(inappProcessRequest
					.getServiceId()+"-"+inappProcessRequest.getMsisdn(), "1");
		}catch(Exception ex){
			logger.error("addToBlock  ",ex);
		}
		return true;
	}

}
