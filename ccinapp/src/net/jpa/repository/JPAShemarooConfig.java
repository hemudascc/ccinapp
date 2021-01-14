package net.jpa.repository;

import java.util.List;

import net.mycomp.shemaroo.ShemarooConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAShemarooConfig extends JpaRepository<ShemarooConfig, Long>{

	@Query("select b from ShemarooConfig b where b.status=:status")
    List<ShemarooConfig> findEnableShemarooConfig(@Param("status")boolean status);

    public ShemarooConfig findByServiceIdAndProductIdAndOperatorNameAndCampId(Integer serviceId,Integer productId,String operatorName,Integer campId);
}