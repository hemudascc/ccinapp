package net.persist.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_subscribers_reg")

public class SubscriberReg implements Serializable{

	@Id
	@Column(name = "subscriber_id", unique = true, nullable = false)
	@GeneratedValue
	private Integer subscriberId;
	@Column(name = "operator_id")
	private Integer operatorId;
	private String msisdn;

	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "reg_date")
	private Timestamp regData;
	
	@Column(name = "status")
	private Integer status;
	@Column(name = "status_descp")
	private String statusDescp;
	
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

	public Integer getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(Integer subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusDescp() {
		return statusDescp;
	}

	public void setStatusDescp(String statusDescp) {
		this.statusDescp = statusDescp;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Timestamp getRegData() {
		return regData;
	}

	public void setRegData(Timestamp regData) {
		this.regData = regData;
	}
}
