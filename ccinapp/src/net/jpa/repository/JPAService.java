package net.jpa.repository;


import net.persist.bean.Service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAService extends JpaRepository<Service, Long>{

	public Service findByAdvertiserIdAndServiceNameAndOpId(Integer advertiserId, String serviceName,Integer opId);

}