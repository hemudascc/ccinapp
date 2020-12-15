package net.jpa.repository;

import java.util.List;

import net.persist.bean.VWCampaignDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPAVWCampaignDetail  extends JpaRepository<VWCampaignDetail, Long>{

	
	@Query("select b from VWCampaignDetail b where b.campaignDetailsStatus=:campaignDetailsStatus")
   List<VWCampaignDetail> findEnableVWCampaignDetail(@Param("campaignDetailsStatus")
    Boolean campaignDetailsStatus);
	
}
