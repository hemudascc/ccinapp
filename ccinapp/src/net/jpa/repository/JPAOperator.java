package net.jpa.repository;
import net.persist.bean.Operator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAOperator extends JpaRepository<Operator, Long> {

	public Operator findByOperatorNameAndAggregatorIdAndCountryId(String operatorName,Integer aggregatorId,Integer countryId);
}
