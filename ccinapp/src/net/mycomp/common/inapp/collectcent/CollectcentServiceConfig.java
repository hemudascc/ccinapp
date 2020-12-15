package net.mycomp.common.inapp.collectcent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_inapp_collectcent_service_config")
public class CollectcentServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "pin_send_api_url")
	private String pinSendUrl;
	
	@Column(name = "pin_validation_url")
	private String pinValidationUrl;
	@Column(name = "status_check_api_url")
	private String checkSubUrl;
	
	@Column(name = "dct_url")
	private String dctUrl;
	
	@Column(name = "amount")
	private Double amount;
	@Column(name = "portal_url")
	private String portalUrl;
	
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getDctUrl() {
		return dctUrl;
	}

	public void setDctUrl(String dctUrl) {
		this.dctUrl = dctUrl;
	}
}
