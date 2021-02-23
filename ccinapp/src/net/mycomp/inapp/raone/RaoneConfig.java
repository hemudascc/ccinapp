package net.mycomp.inapp.raone;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.mycomp.common.inapp.InappAutomatedProcessRequest;

@Entity
@Table(name = "tb_raone_config")
public class RaoneConfig  extends InappAutomatedProcessRequest{

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "operator_name")
	private String operatorName;
	@Column(name = "camp_id")
	private Integer campId;
	@Column(name = "pin_generation_url")
	private String pinGenerationUrl;
	@Column(name = "pin_verification_url")
	private String pinVerificationUrl;
	@Column(name = "status_check_url")
	private String statusCheckUrl;
	@Column(name = "portal_url")
	private String portalUrl;
	@Column(name = "price_point")
	private Double pricePoint;
	@Column(name = "validity")
	private String validity;
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

	public Integer getCampId() {
		return campId;
	}

	public void setCampId(Integer campId) {
		this.campId = campId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getPinGenerationUrl() {
		return pinGenerationUrl;
	}

	public void setPinGenerationUrl(String pinGenerationUrl) {
		this.pinGenerationUrl = pinGenerationUrl;
	}

	public String getPinVerificationUrl() {
		return pinVerificationUrl;
	}

	public void setPinVerificationUrl(String pinVerificationUrl) {
		this.pinVerificationUrl = pinVerificationUrl;
	}

	public String getStatusCheckUrl() {
		return statusCheckUrl;
	}

	public void setStatusCheckUrl(String statusCheckUrl) {
		this.statusCheckUrl = statusCheckUrl;
	}

	public Double getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

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
