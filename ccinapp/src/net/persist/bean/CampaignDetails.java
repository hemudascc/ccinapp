package net.persist.bean;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_campaign_details")
public class CampaignDetails {
		
	@Id
	@GeneratedValue
	@Column(name = "campaign_id") 
	private Integer campaignId;
	@Column(name = "campaign_name")
	private String campaignName;
	@Column(name = "ad_network_id")
	private Integer adNetworkId;
	@Column(name = "callback_url")
	private String callbackUrl;
	@Column(name = "reg_date")
	private Timestamp regDate;	
	@Column(name = "op_id")
	private Integer opId;
	
	private Boolean status;

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Integer getAdNetworkId() {
		return adNetworkId;
	}

	public void setAdNetworkId(Integer adNetworkId) {
		this.adNetworkId = adNetworkId;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
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
