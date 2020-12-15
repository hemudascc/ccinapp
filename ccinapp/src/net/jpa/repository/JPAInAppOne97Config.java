package net.jpa.repository;

import java.util.List;

import net.mycomp.common.inapp.one97.InAppOne97Config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAInAppOne97Config extends JpaRepository<InAppOne97Config, Long>{

	@Query("select b from InAppOne97Config b where b.status=:status")
    List<InAppOne97Config> findEnableOne97Config(@Param("status")boolean status);
    
}