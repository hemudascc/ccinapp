package net.mycomp.inapp.nxmedia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nx_media_service_config")
public class NxMediaServiceConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="service_id")
	private Integer serviceId;
	@Column(name="product_id")
	private Integer productId;
	@Column(name="operator_name")
	private String operatorName;
	@Column(name="pin_send_url")
	private String pinSendURL;
	@Column(name="pin_verify_url")
	private String pinVerifyURL;
	@Column(name="portal_url")
	private String portalURL;
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
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getPinSendURL() {
		return pinSendURL;
	}
	public void setPinSendURL(String pinSendURL) {
		this.pinSendURL = pinSendURL;
	}
	public String getPinVerifyURL() {
		return pinVerifyURL;
	}
	public void setPinVerifyURL(String pinVerifyURL) {
		this.pinVerifyURL = pinVerifyURL;
	}
	public String getPortalURL() {
		return portalURL;
	}
	public void setPortalURL(String portalURL) {
		this.portalURL = portalURL;
	}
	
	@Override
	public String toString() {
		return "NxMediaServiceConfig [id=" + id + ", serviceId=" + serviceId + ", productId=" + productId
				+ ", operatorName=" + operatorName + ", pinSendURL=" + pinSendURL + ", pinVerifyURL=" + pinVerifyURL
				+ ", portalURL=" + portalURL + "]";
	}
}
