package net.jpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import net.persist.bean.CampaignDetails;

public interface JPACampaignDetails extends JpaRepository<CampaignDetails, Long> {

	public CampaignDetails findByCampaignNameAndAdNetworkIdAndOpId(String campaignName,Integer adNetworkId,Integer opId );
}
