package net.mycomp.inapp.apalya;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_apalya_api_trans")
public class ApalyaApiTrans implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	private String action;	
	@Column(name = "operator_name")
	private String operatoName;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "apalya_campid")
	private Integer apalyaCampid;
	private String msisdn;
	@Column(name = "token_to_cg")
	private String tokenToCg;
	private String request;
	private String response;
	private Boolean success;
	@Column(name="portal_url")
	private String portalURL;
	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public ApalyaApiTrans(){}
	
	public ApalyaApiTrans(boolean status){
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

	public Integer getApalyaCampid() {
		return apalyaCampid;
	}

	public void setApalyaCampid(Integer apalyaCampid) {
		this.apalyaCampid = apalyaCampid;
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

	public String getPortalURL() {
		return portalURL;
	}

	public void setPortalURL(String portalURL) {
		this.portalURL = portalURL;
	}
	
	
}
