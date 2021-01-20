package net.mycomp.inapp.raone;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.common.jms.JMSService;
import net.jpa.repository.JPARaoneConfig;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.common.inapp.AbstractInappOperatorServiceApi;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.service.IDaoService;
import net.mycomp.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;

@Service("inAppRaoneService")
public class RaoneService extends AbstractInappOperatorServiceApi {

	private static final Logger logger = Logger.getLogger(RaoneService.class.getName());

	@Autowired
	private JPARaoneConfig jpaRaoneConfig;

	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Autowired
	private RaoneServiceApi raoneServiceApi;
	@Value("${jdbc.db.name}")
	private String dbName;

	@PostConstruct
	public void init() {

		List<RaoneConfig> listInAppConfig = jpaRaoneConfig.findEnableRaoneConfig(true);
		RaoneConstant.mapServiceIdToRaoneConfig
				.putAll(listInAppConfig.stream().collect(Collectors.toMap(p -> p.getServiceId(), p -> p)));
	}

	public boolean sendPin(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {

		try {

			RaoneConfig raoneConfig = RaoneConstant.mapServiceIdToRaoneConfig.get(inappProcessRequest.getServiceId());
			RaoneApiTrans sendPinRaoneApiTrans = raoneServiceApi.sendPin(inappProcessRequest, raoneConfig);
			if (sendPinRaoneApiTrans.getSuccess()) {
				inappProcessRequest.setSuccess(true);
			}
		} catch (Exception ex) {
			logger.error("sendPin ", ex);

		} finally {
		}
		return true;
	}

	public boolean validatePin(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {

		try {
			RaoneConfig raoneConfig = RaoneConstant.mapServiceIdToRaoneConfig.get(inappProcessRequest.getServiceId());

			RaoneApiTrans validatePinRaoneApiTrans = raoneServiceApi.validatePin(inappProcessRequest, raoneConfig);
			if (validatePinRaoneApiTrans.getSuccess()) {
				inappProcessRequest.setSuccess(true);
			}
		} catch (Exception ex) {
			logger.error("validatePin ", ex);
		} finally {
		}
		return true;

	}

	public boolean statusCheck(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {

		try {
			List<SubscriberReg> subscriberReg = jpaSubscriberReg
					.findSubscriberRegByMsisdn(inappProcessRequest.getMsisdn());
			if (Objects.nonNull(subscriberReg) && subscriberReg.get(0).getStatus() == 1) {
				inappProcessRequest.setStatus(true);
			}

		} catch (Exception ex) {
			logger.error("statusCheck ", ex);
		} finally {
		}
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {

		try {
			RaoneConfig raoneConfig = RaoneConstant.mapServiceIdToRaoneConfig.get(inappProcessRequest.getServiceId());

			if (raoneConfig != null) {
				return raoneConfig.getPortalUrl();
			}
		} catch (Exception ex) {
			logger.error("portalUrl ", ex);
		} finally {
		}
		return null;
	}
}
