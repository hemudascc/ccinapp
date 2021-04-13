package net.mycomp.common.inapp;

import java.lang.reflect.Field;
import java.sql.Timestamp;

public class UniqueCount {

	private int id;
	private int cmpId;
	private Timestamp reportDate;
	private String serviceName;
	private String advertiserName;
	private int uniquePinRequestCount;
	private int uniquePinSendCount;
	private int uniquePinValidateCount;
	private String createtime;
	
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

	public Timestamp getReportDate() {
		return reportDate;
	}

	public void setReportDate(Timestamp reportDate) {
		this.reportDate = reportDate;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public int getUniquePinRequestCount() {
		return uniquePinRequestCount;
	}

	public void setUniquePinRequestCount(int uniquePinRequestCount) {
		this.uniquePinRequestCount = uniquePinRequestCount;
	}

	public int getUniquePinSendCount() {
		return uniquePinSendCount;
	}

	public void setUniquePinSendCount(int uniquePinSendCount) {
		this.uniquePinSendCount = uniquePinSendCount;
	}

	public int getUniquePinValidateCount() {
		return uniquePinValidateCount;
	}

	public void setUniquePinValidateCount(int uniquePinValidateCount) {
		this.uniquePinValidateCount = uniquePinValidateCount;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

}
