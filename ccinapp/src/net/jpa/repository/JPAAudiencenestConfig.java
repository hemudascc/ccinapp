package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.inapp.audiencenest.AudiencenestConfig;

public interface JPAAudiencenestConfig extends JpaRepository<AudiencenestConfig, Long> {

	
	
	@Query("select b from AudiencenestConfig b where b.status=:status")
    List<AudiencenestConfig> findEnableAudiencenestConfig(@Param("status")boolean status);
}
