package net.jpa.repository;

import java.util.List;

import net.mycomp.common.inapp.collectcent.CollectcentServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPACollectcentServiceConfig extends JpaRepository<CollectcentServiceConfig, Long>{

	@Query("select b from CollectcentServiceConfig b where b.status=:status")
    List<CollectcentServiceConfig> findEnableCollectcentServiceConfig(@Param("status")boolean status);
    
}