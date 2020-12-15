package net.jpa.repository;

import net.persist.bean.VWTrafficRouting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPAVWTrafficRouting extends JpaRepository<VWTrafficRouting, Long>{

	@Query("select b from VWTrafficRouting b where b.trafiicRoutingId=:trafiicRoutingId")
	VWTrafficRouting findVWTrafficRoutingByTraffingId(@Param("trafiicRoutingId")Integer trafiicRoutingId);
}