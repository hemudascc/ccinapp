package net.jpa.repository;

import java.util.List;

import net.mycomp.advertiser.vaca.VacaConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAVacaConfig extends JpaRepository<VacaConfig, Long>{

	@Query("select b from VacaConfig b where b.status=:status")
    List<VacaConfig> findEnableVacaConfig(@Param("status")boolean status);

	public VacaConfig findByServiceIdAndProductIdAndOperatorName(Integer serviceId,Integer productId,String operatorName);
   
}