package net.persist.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.mycomp.common.service.RedisCacheService;
import net.util.MConstants;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;





import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Convert;

@Entity
@Table(name = "vw_traffic_routing")
public class VWTrafficRouting implements Serializable,Comparable<VWTrafficRouting> {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(VWTrafficRouting.class);

	@Id
	@Column(name = "trafiic_routing_id", nullable = false)
	private Integer trafiicRoutingId;
	@Column(name = "ad_network_id")
	private Integer adnetworkId;
	@Column(name = "adnetwork_name")
	private String adnetworkName;
	@Column(name = "campaign_id")
	private Integer campaignId;
	@Column(name = "campaign_name")
	private String campaignName;
	@Column(name="campaign_operator_id")
	private Integer campaignOperatorId;
	@Column(name="campaign_operator_name")
	private String campaignOperatorName;
	
	
	
	@Column(name = "advertiser_id")
	private Integer advertiserId;
	@Column(name = "advertiser_name")
	private String advertiserName;	
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "service_name")
	private String serviceName;
	@Column(name="advertiser_operator_id")
	private Integer advertiserOperatorId;	
	@Column(name = "advertiser_operator_name")
	private String advertiserOperatorName;	
	@Column(name = "percentage_of_traffic")
	private Integer percentageOfTraffic;
	
	@Column(name = "trafiic_routing_status")
	private Boolean trafiicRoutingStatus;
	
	@Transient
	@JsonIgnore
	public AtomicInteger atomicIntegerclickCounter = new AtomicInteger(0);
	
	public boolean isSendPin(List<SubscriberReg> listSubbscriberReg) {
//		for(SubscriberReg subscriberReg:listSubbscriberReg){
//			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED
//					&&subscriberReg.getServiceId()==serviceId){
//				return false;
//			 } 
//		}		
	return atomicIntegerclickCounter.getAndUpdate(n -> n + 1) < percentageOfTraffic;	
	}
		
	public static void main(String arg[]){
	}

	@Override
	public int compareTo(VWTrafficRouting o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Integer getTrafiicRoutingId() {
		return trafiicRoutingId;
	}

	public void setTrafiicRoutingId(Integer trafiicRoutingId) {
		this.trafiicRoutingId = trafiicRoutingId;
	}

	public Integer getAdnetworkId() {
		return adnetworkId;
	}

	public void setAdnetworkId(Integer adnetworkId) {
		this.adnetworkId = adnetworkId;
	}

	public String getAdnetworkName() {
		return adnetworkName;
	}

	public void setAdnetworkName(String adnetworkName) {
		this.adnetworkName = adnetworkName;
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

	

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getPercentageOfTraffic() {
		return percentageOfTraffic;
	}

	public void setPercentageOfTraffic(Integer percentageOfTraffic) {
		this.percentageOfTraffic = percentageOfTraffic;
	}

	public Boolean getTrafiicRoutingStatus() {
		return trafiicRoutingStatus;
	}

	public void setTrafiicRoutingStatus(Boolean trafiicRoutingStatus) {
		this.trafiicRoutingStatus = trafiicRoutingStatus;
	}

	public AtomicInteger getAtomicIntegerclickCounter() {
		return atomicIntegerclickCounter;
	}

	public void setAtomicIntegerclickCounter(AtomicInteger atomicIntegerclickCounter) {
		this.atomicIntegerclickCounter = atomicIntegerclickCounter;
	}

	

	public Integer getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	

	public Integer getCampaignOperatorId() {
		return campaignOperatorId;
	}

	public void setCampaignOperatorId(Integer campaignOperatorId) {
		this.campaignOperatorId = campaignOperatorId;
	}

	public String getCampaignOperatorName() {
		return campaignOperatorName;
	}

	public void setCampaignOperatorName(String campaignOperatorName) {
		this.campaignOperatorName = campaignOperatorName;
	}

	public Integer getAdvertiserOperatorId() {
		return advertiserOperatorId;
	}

	public void setAdvertiserOperatorId(Integer advertiserOperatorId) {
		this.advertiserOperatorId = advertiserOperatorId;
	}

	public String getAdvertiserOperatorName() {
		return advertiserOperatorName;
	}

	public void setAdvertiserOperatorName(String advertiserOperatorName) {
		this.advertiserOperatorName = advertiserOperatorName;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
		
}
