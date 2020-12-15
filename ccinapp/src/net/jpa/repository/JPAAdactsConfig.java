package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.inapp.adacts.AdactsConfig;

public interface JPAAdactsConfig extends JpaRepository<AdactsConfig, Long>{

	@Query("select b from AdactsConfig b where b.status=:status")
    List<AdactsConfig> findEnableAdactsConfig(@Param("status")boolean status);

}
