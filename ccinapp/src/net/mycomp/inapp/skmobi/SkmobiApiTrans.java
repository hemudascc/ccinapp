package net.mycomp.inapp.skmobi;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_skmobi_api_trans")
public class SkmobiApiTrans implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	private String action;	
	@Column(name = "operator_name")
	private String operatoName;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "sk_mobi_campid")
	private Integer skMobiCampid;
	private String msisdn;
	@Column(name = "token_to_cg")
	private String tokenToCg;
	private String request;
	private String response;
	private Boolean success;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public SkmobiApiTrans(){}
	
	public SkmobiApiTrans(boolean status){
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
	public Integer getSkMobiCampid() {
		return skMobiCampid;
	}
	public void setSkMobiCampid(Integer skMobiCampid) {
		this.skMobiCampid = skMobiCampid;
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

	public String getOperatoName() {
		return operatoName;
	}

	public void setOperatoName(String operatoName) {
		this.operatoName = operatoName;
	}
	
}
