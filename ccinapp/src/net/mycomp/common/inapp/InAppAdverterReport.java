package net.mycomp.common.inapp;

import java.lang.reflect.Field;
import java.sql.Timestamp;


public class InAppAdverterReport {

	private int id;
	private Timestamp reportDate;
	
	private int cmpId;
	private int advertiserId;	
	private int serviceId;
	private String advertiserName;
	private String actionType;
	private String advertiserApiRequest;
	private String advertiserApiResponse;
	private String serviceName;
	private Timestamp createDate;
	private String reportDateStr;
	
	
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


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public int getCmpId() {
			return cmpId;
		}


		public void setCmpId(int cmpId) {
			this.cmpId = cmpId;
		}


		public int getAdvertiserId() {
			return advertiserId;
		}


		public void setAdvertiserId(int advertiserId) {
			this.advertiserId = advertiserId;
		}


		public int getServiceId() {
			return serviceId;
		}


		public void setServiceId(int serviceId) {
			this.serviceId = serviceId;
		}


		public String getAdvertiserName() {
			return advertiserName;
		}


		public void setAdvertiserName(String advertiserName) {
			this.advertiserName = advertiserName;
		}



		public String getActionType() {
			return actionType;
		}


		public void setActionType(String actionType) {
			this.actionType = actionType;
		}


		public String getAdvertiserApiRequest() {
			return advertiserApiRequest;
		}


		public void setAdvertiserApiRequest(String advertiserApiRequest) {
			this.advertiserApiRequest = advertiserApiRequest;
		}


		public String getAdvertiserApiResponse() {
			return advertiserApiResponse;
		}


		public void setAdvertiserApiResponse(String advertiserApiResponse) {
			this.advertiserApiResponse = advertiserApiResponse;
		}


		public String getServiceName() {
			return serviceName;
		}


		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}


		public Timestamp getCreateDate() {
			return createDate;
		}


		public void setCreateDate(Timestamp createDate) {
			this.createDate = createDate;
		}


		public Timestamp getReportDate() {
			return reportDate;
		}


		public void setReportDate(Timestamp reportDate) {
			this.reportDate = reportDate;
		}


		public String getReportDateStr() {
			return reportDateStr;
		}


		public void setReportDateStr(String reportDateStr) {
			this.reportDateStr = reportDateStr;
		}

}
