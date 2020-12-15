package net.persist.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "traffic_routing")
public class TrafficRouting {

	@Id
	@Column(name = "trafiic_routing_id", unique = true, nullable = false)
	@GeneratedValue
	private Integer trafiicRoutingId;
	@Column(name = "campaign_id")
	private Integer campaignId;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "percentage_of_traffic")
	private Integer percentageOfTraffic;
	@Column(name = "trafiic_routing_status")
	private Boolean trafiicRoutingStatus;
	
	public Integer getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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
	public Integer getTrafiicRoutingId() {
		return trafiicRoutingId;
	}
	public void setTrafiicRoutingId(Integer trafiicRoutingId) {
		this.trafiicRoutingId = trafiicRoutingId;
	}
}
