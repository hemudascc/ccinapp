package net.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.persist.bean.AdnetworkOperatorConfig;

public interface JPAAdnetworkOperatorConfig extends JpaRepository<AdnetworkOperatorConfig, Long>{

	public AdnetworkOperatorConfig findByOperatorIdAndAdNetworkId(Integer operatorId,Integer adNetworkId);
}