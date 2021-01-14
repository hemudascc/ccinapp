package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.inapp.adacts.AdactsConfig;
import net.mycomp.inapp.apalya.ApalyaConfig;

public interface JPAApalyaConfig extends JpaRepository<ApalyaConfig, Long>{

	@Query("select b from ApalyaConfig b where b.status=:status")
    List<ApalyaConfig> findEnableApalyaConfig(@Param("status")boolean status);  

	public ApalyaConfig findByServiceIdAndProductIdAndOperatorNameAndCampId(Integer serviceId,Integer productId,String operatorName,Integer campId);

}
