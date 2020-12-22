package net.jpa.repository;

import java.util.List;
import net.persist.bean.SubscriberReg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPASubscriberReg extends JpaRepository<SubscriberReg, Long>{
	
    @Query("select b from SubscriberReg b where b.subscriberId=:subscriberId")
    SubscriberReg findSubscriberRegById(@Param("subscriberId")Integer subscriberId);
	
    @Query("select b from SubscriberReg b where b.msisdn=:msisdn")
    List<SubscriberReg> findSubscriberRegByMsisdn(@Param("msisdn")String msisdn);
	
	@Query("select b from SubscriberReg b where b.msisdn=:msisdn and b.serviceId=:serviceId")
    SubscriberReg findSubscriberRegByMsisdnAndServiceId(@Param("msisdn")String msisdn,
    		@Param("serviceId")Integer serviceId);
	
	
}




