package net.jpa.repository;

import java.util.List;

import net.mycomp.common.inapp.tmt.InAppTmtConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAInappTmtConfig extends JpaRepository<InAppTmtConfig, Long>{

	@Query("select b from InAppTmtConfig b where b.status=:status")
    List<InAppTmtConfig> findEnableTmtInAppConfig(@Param("status")boolean status);
	
	public InAppTmtConfig findByServiceIdAndOperatorName(Integer serviceId,String operarorName);

    
}