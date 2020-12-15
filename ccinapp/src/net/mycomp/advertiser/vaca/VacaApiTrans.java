package net.mycomp.advertiser.vaca;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_vaca_api_trans")
public class VacaApiTrans implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	private String action;	
	@Column(name = "cmp_id")
	private Integer cmpId;
	@Column(name = "service_id")
	private Integer serviceId;
	private String msisdn;
	@Column(name = "token_to_cg")
	private String tokenToCg;
	private String request;
	private String response;
	@Column(name="response_code")
	private String responseCode;
	private Boolean success;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public VacaApiTrans(){}
	
	public VacaApiTrans(boolean status){
		this.status=status;
		this.success=false;
		this.createTime=new Timestamp(System.currentTimeMillis());		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getTokenToCg() {
		return tokenToCg;
	}

	public void setTokenToCg(String tokenToCg) {
		this.tokenToCg = tokenToCg;
	}



	public Integer getCmpId() {
		return cmpId;
	}

	public void setCmpId(Integer cmpId) {
		this.cmpId = cmpId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
}
