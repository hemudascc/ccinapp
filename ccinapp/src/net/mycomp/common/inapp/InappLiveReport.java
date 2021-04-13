package net.mycomp.common.inapp;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;

import net.util.MConstants;

public class InappLiveReport {

	private int id;
	private Timestamp reportDate;
	
	private int operatorId;
	private String operatorName;
	private int adnetworkId; 
	private int aggregatorId;	
	private String networkName;
	private String reportDateStr;
	private int adnetworkCampaignId;
	private String pubId = MConstants.DEFAULT_PUB_ID;
	private int pinRequestCount;
	private int pinSendCount;
	private int pinValidationRequestCount;
	private int pinValidateCount;
	private double pinValidateAmount;
	private int statusCheckRequestCount;  
	private int sendConversionCount;
	private double sendConversionAmount;
	private int actionHours;
	private Integer serviceId;
	private String serviceName;
	private int productId;
	private Timestamp lastUpdateTime;
	private String advertiserName;
	
		public String toString() {

			Field[] fields = this.getClass().getDeclaredFields();
			String str = this.getClass().getName();
			try {
				for (Field field : fields) {
					str += field.getName() + "=" + field.get(this) + ",";
				}
			} catch (IllegalArgumentException ex) {
				System.out.println(ex);
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			return str;
		}

	public InappLiveReport(){		
		}

		public InappLiveReport(int operatorId, Timestamp timestamp, Integer adnetworkCampaignId,int serviceId,int productId) {

			this.operatorId = operatorId;
			this.reportDate = timestamp;
			
			if (adnetworkCampaignId == null) {
				adnetworkCampaignId = MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID;		
			}
			this.serviceId=serviceId;
			this.adnetworkCampaignId = adnetworkCampaignId;
			this.productId=productId;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Timestamp getReportDate() {
			return reportDate;
		}

		public void setReportDate(Timestamp reportDate) {
			this.reportDate = reportDate;
		}

		

		public int getOperatorId() {
			return operatorId;
		}

		public void setOperatorId(int operatorId) {
			this.operatorId = operatorId;
		}

		public int getAdnetworkCampaignId() {
			return adnetworkCampaignId;
		}

		public void setAdnetworkCampaignId(int adnetworkCampaignId) {
			this.adnetworkCampaignId = adnetworkCampaignId;
		}

		public String getPubId() {
			return pubId;
		}

		public void setPubId(String pubId) {
			this.pubId = pubId;
		}

		public int getPinRequestCount() {
			return pinRequestCount;
		}

		public void setPinRequestCount(int pinRequestCount) {
			this.pinRequestCount = pinRequestCount;
		}

		public int getPinSendCount() {
			return pinSendCount;
		}

		public void setPinSendCount(int pinSendCount) {
			this.pinSendCount = pinSendCount;
		}

		public int getPinValidationRequestCount() {
			return pinValidationRequestCount;
		}

		public void setPinValidationRequestCount(int pinValidationRequestCount) {
			this.pinValidationRequestCount = pinValidationRequestCount;
		}

		public int getPinValidateCount() {
			return pinValidateCount;
		}

		public void setPinValidateCount(int pinValidateCount) {
			this.pinValidateCount = pinValidateCount;
		}

		public double getPinValidateAmount() {
			return pinValidateAmount;
		}

		public void setPinValidateAmount(double pinValidateAmount) {
			this.pinValidateAmount = pinValidateAmount;
		}

		public int getStatusCheckRequestCount() {
			return statusCheckRequestCount;
		}

		public void setStatusCheckRequestCount(int statusCheckRequestCount) {
			this.statusCheckRequestCount = statusCheckRequestCount;
		}

		public int getSendConversionCount() {
			return sendConversionCount;
		}

		public void setSendConversionCount(int sendConversionCount) {
			this.sendConversionCount = sendConversionCount;
		}

		public double getSendConversionAmount() {
			return sendConversionAmount;
		}

		public void setSendConversionAmount(double sendConversionAmount) {
			this.sendConversionAmount = sendConversionAmount;
		}

		public int getActionHours() {
			return actionHours;
		}

		public void setActionHours(int actionHours) {
			this.actionHours = actionHours;
		}

		

		public int getProductId() {
			return productId;
		}

		public void setProductId(int productId) {
			this.productId = productId;
		}

		public int getAdnetworkId() {
			return adnetworkId;
		}

		public void setAdnetworkId(int adnetworkId) {
			this.adnetworkId = adnetworkId;
		}

		public int getAggregatorId() {
			return aggregatorId;
		}

		public void setAggregatorId(int aggregatorId) {
			this.aggregatorId = aggregatorId;
		}

		public String getOperatorName() {
			return operatorName;
		}

		public void setOperatorName(String operatorName) {
			this.operatorName = operatorName;
		}

		public String getReportDateStr() {
			return reportDateStr;
		}

		public void setReportDateStr(String reportDateStr) {
			this.reportDateStr = reportDateStr;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public String getNetworkName() {
			return networkName;
		}

		public void setNetworkName(String networkName) {
			this.networkName = networkName;
		}

		public void setServiceId(Integer serviceId) {
			this.serviceId = serviceId;
		}

		public Integer getServiceId() {
			return serviceId;
		}

		public Timestamp getLastUpdateTime() {
			return lastUpdateTime;
		}

		public void setLastUpdateTime(Timestamp lastUpdateTime) {
			this.lastUpdateTime = lastUpdateTime;
		}

		public String getAdvertiserName() {
			return advertiserName;
		}

		public void setAdvertiserName(String advertiserName) {
			this.advertiserName = advertiserName;
		}

		
}
