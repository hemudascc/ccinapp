package net.jpa.repository;

import java.util.List;

import net.mycomp.ascenco.AscencoConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAAscencoConfig extends JpaRepository<AscencoConfig, Long>{

	@Query("select b from AscencoConfig b where b.status=:status")
    List<AscencoConfig> findEnableAscencoConfig(@Param("status")boolean status);

	public AscencoConfig findByServiceIdAndProductIdAndOperatorNameAndCampId(Integer serviceId,Integer productId,String operatorName,Integer campId);

}