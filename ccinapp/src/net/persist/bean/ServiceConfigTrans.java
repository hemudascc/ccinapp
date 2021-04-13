package net.persist.bean;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_service_config_trans")
public class ServiceConfigTrans {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="advertiser_id")
	private int advertiserid;
	@Column(name="product_id")
	private int productid;
	@Column(name="operator_id")
	private int operatorid;
	@Column(name="authrization")
	private String authrization;
	@Column(name="pin_send_url")
	private String pinsendurl;
	@Column(name="resend_pin_url")
	private String resendpinurl;
	@Column(name="pin_validation_url")
	private String pinvalidationurl;
	@Column(name="status_check_url")
	private String statuscheckurl;
	@Column(name="portal_url")
	private String portalurl;
	@Column(name="dct_url")
	private String dcturl;
	@Column(name="request_status")
	private String requestStatus;
	@Column(name="query_string")
	private String queryString;
	@Column(name="otp_length")
	private int otpLength;
	@Column(name="create_time")
	private Timestamp createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAdvertiserid() {
		return advertiserid;
	}
	public void setAdvertiserid(int advertiserid) {
		this.advertiserid = advertiserid;
	}
	public int getProductid() {
		return productid;
	}
	public void setProductid(int productid) {
		this.productid = productid;
	}
	public int getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(int operatorid) {
		this.operatorid = operatorid;
	}
	public String getAuthrization() {
		return authrization;
	}
	public void setAuthrization(String authrization) {
		this.authrization = authrization;
	}
	public String getPinsendurl() {
		return pinsendurl;
	}
	public void setPinsendurl(String pinsendurl) {
		this.pinsendurl = pinsendurl;
	}
	public String getResendpinurl() {
		return resendpinurl;
	}
	public void setResendpinurl(String resendpinurl) {
		this.resendpinurl = resendpinurl;
	}
	public String getPinvalidationurl() {
		return pinvalidationurl;
	}
	public void setPinvalidationurl(String pinvalidationurl) {
		this.pinvalidationurl = pinvalidationurl;
	}
	public String getStatuscheckurl() {
		return statuscheckurl;
	}
	public void setStatuscheckurl(String statuscheckurl) {
		this.statuscheckurl = statuscheckurl;
	}
	public String getPortalurl() {
		return portalurl;
	}
	public void setPortalurl(String portalurl) {
		this.portalurl = portalurl;
	}
	public String getDcturl() {
		return dcturl;
	}
	public void setDcturl(String dcturl) {
		this.dcturl = dcturl;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}	
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public int getOtpLength() {
		return otpLength;
	}
	public void setOtpLength(int otpLength) {
		this.otpLength = otpLength;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
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

}
