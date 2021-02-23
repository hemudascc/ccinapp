package net.mycomp.common.inapp.tmt;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.mycomp.common.inapp.InappAutomatedProcessRequest;

import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_inapp_tmt_config")
public class InAppTmtConfig  extends InappAutomatedProcessRequest{

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "pin_send_url")
	private String pinSendUrl;	
	@Column(name = "pin_validation_url")
	private String pinValidationUrl;
	@Column(name = "check_sub_url")
	private String checkSubUrl;
	
	@Column(name = "amount")
	private Double amount;
	@Column(name = "portal_url")
	private String portalUrl;
	
	@Column(name = "portal_url2")
	private String portalUrl2;
	
	@Column(name = "operator_name")
	private String operatorName;
	
	private Boolean status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getPinSendUrl() {
		return pinSendUrl;
	}
	public void setPinSendUrl(String pinSendUrl) {
		this.pinSendUrl = pinSendUrl;
	}
	public String getPinValidationUrl() {
		return pinValidationUrl;
	}
	public void setPinValidationUrl(String pinValidationUrl) {
		this.pinValidationUrl = pinValidationUrl;
	}
	public String getCheckSubUrl() {
		return checkSubUrl;
	}
	public void setCheckSubUrl(String checkSubUrl) {
		this.checkSubUrl = checkSubUrl;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}
	public String getPortalUrl2() {
		return portalUrl2;
	}
	public void setPortalUrl2(String portalUrl2) {
		this.portalUrl2 = portalUrl2;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	  
	
}
