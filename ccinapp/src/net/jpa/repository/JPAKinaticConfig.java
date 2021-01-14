package net.jpa.repository;

import java.util.List;

import net.mycomp.kineticdigital.KinaticConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAKinaticConfig extends JpaRepository<KinaticConfig, Long>{

	@Query("select b from KinaticConfig b where b.status=:status")
    List<KinaticConfig> findEnableKinaticConfig(@Param("status")boolean status);

    public KinaticConfig findByServiceIdAndProductIdAndOperatorNameAndCampId(Integer serviceId,Integer productId,String operatorName,Integer campId);;
}