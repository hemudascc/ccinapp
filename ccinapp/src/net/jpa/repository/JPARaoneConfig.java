package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.inapp.raone.RaoneConfig;

public interface JPARaoneConfig extends JpaRepository<RaoneConfig, Long> {

	@Query("select b from RaoneConfig b where b.status=:status")
    List<RaoneConfig> findEnableRaoneConfig(@Param("status")boolean status);  

	public RaoneConfig findByServiceIdAndOperatorName(Integer serviceId, String operatorName);
}
