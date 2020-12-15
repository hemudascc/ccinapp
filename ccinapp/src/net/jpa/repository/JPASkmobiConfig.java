package net.jpa.repository;

import java.util.List;

import net.mycomp.common.inapp.tmt.InAppTmtConfig;
import net.mycomp.inapp.skmobi.SkmobiConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPASkmobiConfig extends JpaRepository<SkmobiConfig, Long>{

	@Query("select b from SkmobiConfig b where b.status=:status")
    List<SkmobiConfig> findEnableSkmobiConfig(@Param("status")boolean status);

    
}