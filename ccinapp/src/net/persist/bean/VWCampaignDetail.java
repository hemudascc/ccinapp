package net.persist.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Table(name = "vw_campaign_detail")

public class VWCampaignDetail implements Serializable{

	@Id
	@Column(name = "campaign_id", nullable = false)
	private Integer campaignId;
	
	@Column(name = "campaign_name")
	private String  campaignName;
	
	@Column(name = "ad_network_id")
	private Integer adNetworkId;
	@Column(name = "callback_url")
	private String  callbackUrl;
	@Column(name = "reg_date")
	private Timestamp  regDate;
	@Column(name = "campaign_details_status")
	private Boolean campaignDetailsStatus;
	
	@Column(name = "op_id")
	private Integer opId;
	
	@Column(name = "network_name")
	private String  networkName;
	@Column(name = "operator_name")
	private String  operatorName;
	
	
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


public Boolean getCampaignDetailsStatus() {
	return campaignDetailsStatus;
}


public void setCampaignDetailsStatus(Boolean campaignDetailsStatus) {
	this.campaignDetailsStatus = campaignDetailsStatus;
}


public Integer getOpId() {
	return opId;
}


public void setOpId(Integer opId) {
	this.opId = opId;
}


public String getNetworkName() {
	return networkName;
}


public void setNetworkName(String networkName) {
	this.networkName = networkName;
}


public String getOperatorName() {
	return operatorName;
}


public void setOperatorName(String operatorName) {
	this.operatorName = operatorName;
}

	
}
