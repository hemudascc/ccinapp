package net.persist.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_service")
public class Service {

	@Id
	@Column(name = "service_id")
	@GeneratedValue
	private Integer serviceId;
	@Column(name = "service_name")
	private String serviceName;
	@Column(name = "service_desc")
	private String serviceDesc;
	@Column(name = "op_id")
	private Integer opId;
	@Column(name = "advertiser_id")
	private Integer advertiserId;
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "otp_length")
	private Integer otpLength;
	@Column(name = "validity")
	private Integer validity;
	@Column(name = "price_point")
	private Integer pricePoint;

	private Boolean status;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public Integer getOpId() {
		return opId;
	}

	public void setOpId(Integer opId) {
		this.opId = opId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getAdvertiserId() {
		return advertiserId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public Integer getOtpLength() {
		return otpLength;
	}

	public void setOtpLength(Integer otpLength) {
		this.otpLength = otpLength;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public Integer getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(Integer pricePoint) {
		this.pricePoint = pricePoint;
	}

	@Override
	public String toString() {
		return "Service [serviceId=" + serviceId + ", serviceName=" + serviceName + ", serviceDesc=" + serviceDesc
				+ ", opId=" + opId + ", advertiserId=" + advertiserId + ", productId=" + productId + ", otpLength="
				+ otpLength + ", validity=" + validity + ", pricePoint=" + pricePoint + ", status=" + status + "]";
	}

}
