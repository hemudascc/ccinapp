package net.jpa.repository;

import java.util.List;

import net.persist.bean.TrafficRouting;
import net.persist.bean.VWTrafficRouting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPATrafficRouting extends JpaRepository<TrafficRouting, Long>{

	@Query("select b from TrafficRouting b where b.trafiicRoutingId=:trafiicRoutingId")
	TrafficRouting findTrafficRoutingByTraffingId(@Param("trafiicRoutingId")Integer trafiicRoutingId);

}