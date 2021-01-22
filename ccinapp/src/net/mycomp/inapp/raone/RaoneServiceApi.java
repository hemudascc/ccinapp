package net.mycomp.inapp.raone;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.jms.JMSService;
import net.mycomp.common.inapp.InappProcessRequest;
import net.mycomp.common.service.RedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MConstants;

@Service("inAppRaoneServiceApi")
public class RaoneServiceApi {

	private static final Logger logger = Logger.getLogger(RaoneServiceApi.class.getName());

	@Autowired
	private JMSService jmsService;

	@Autowired
	private RedisCacheService redisCacheService;

	private HttpURLConnectionUtil httpURLConnectionUtil;

	@PostConstruct
	public void init() {
		httpURLConnectionUtil = new HttpURLConnectionUtil();
	}

	public RaoneApiTrans sendPin(InappProcessRequest inappProcessRequest, RaoneConfig raoneConfig) {

		RaoneApiTrans raoneApiTrans = null;
		boolean resend = false;
		try {

			raoneApiTrans = new RaoneApiTrans(true);
			raoneApiTrans.setAction(MConstants.ACTION_PIN_SENT);
			raoneApiTrans.setMsisdn(inappProcessRequest.getMsisdn());
			raoneApiTrans.setRaoneCampid(raoneConfig.getCampId());
			raoneApiTrans.setServiceId(raoneConfig.getServiceId());
			raoneApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
			raoneApiTrans.setOperatoName(raoneConfig.getOperatorName());

			if (Objects.toString(redisCacheService
					.getObjectCacheValue(RaoneConstant.RAONE_RESEND_OTP_PREFIX + inappProcessRequest.getMsisdn()))
					.equals("1")) {
				resend = true;
			}

			String url = RaoneConstant.getUrl(raoneConfig.getPinGenerationUrl(), resend,
					inappProcessRequest.getMsisdn(), raoneConfig, null, inappProcessRequest.getCgToken());
			raoneApiTrans.setRequest(url);

			HTTPResponse httpResponse = httpURLConnectionUtil.makeHTTPGETRequest(url, null);
			raoneApiTrans.setResponse(httpResponse.getResponseCode() + " : " + httpResponse.getResponseStr());

			if (httpResponse.getResponseCode() == 200
					&& (Objects.toString(httpResponse.getResponseStr()).contains("SUCCESS") || Objects
							.toString(httpResponse.getResponseStr()).contains("The pin code has been sent again"))) {// pin_sent
				// Map map= JsonMapper.getJsonToObject(httpResponse.getResponseStr(),
				// Map.class);
				logger.info("send pin request msisdn=" + raoneApiTrans.getMsisdn() + " :: response="
						+ httpResponse.getResponseStr());
				// SUCCESS
				redisCacheService.putObjectCacheValueByEvictionMinute(
						RaoneConstant.RAONE_RESEND_OTP_PREFIX + inappProcessRequest.getMsisdn(), "1", 5);
				inappProcessRequest.setSuccess(true);
				raoneApiTrans.setSuccess(true);
			}
		} catch (Exception ex) {
			logger.error("sendPin ", ex);

		} finally {
			jmsService.saveObject(raoneApiTrans);
		}
		return raoneApiTrans;
	}

	public RaoneApiTrans validatePin(InappProcessRequest inappProcessRequest, RaoneConfig raoneConfig) {

		RaoneApiTrans raoneApiTrans = null;
		try {

			raoneApiTrans = new RaoneApiTrans(true);
			raoneApiTrans.setAction(MConstants.ACTION_PIN_VERFICATION);
			raoneApiTrans.setOperatoName(raoneConfig.getOperatorName());
			raoneApiTrans.setMsisdn(inappProcessRequest.getMsisdn());
			raoneApiTrans.setRaoneCampid(raoneConfig.getCampId());
			raoneApiTrans.setServiceId(raoneConfig.getServiceId());
			raoneApiTrans.setTokenToCg(inappProcessRequest.getCgToken());
			String url = RaoneConstant.getUrl(raoneConfig.getPinVerificationUrl(), false,
					inappProcessRequest.getMsisdn(), raoneConfig, inappProcessRequest.getPin(), null);
			raoneApiTrans.setRequest(url);

			HTTPResponse httpResponse = httpURLConnectionUtil.makeHTTPGETRequest(url, null);
			raoneApiTrans.setResponse(httpResponse.getResponseCode() + " : " + httpResponse.getResponseStr());

			if (httpResponse.getResponseCode() == 200
					&& Objects.toString(httpResponse.getResponseStr()).contains("SUCCESS")) {// pin_sent
				// SUCCESS
				raoneApiTrans.setSuccess(true);
			}

		} catch (Exception ex) {
			logger.error("sendPin ", ex);

		} finally {
			jmsService.saveObject(raoneApiTrans);
		}
		return raoneApiTrans;
	}

}
