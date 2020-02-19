package com.djedra.repository.exchangeratestable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.djedra.entity.exchangeratestable.Rates;

public interface IRatesRepository extends JpaRepository<Rates, Long>{

	@Query(value = "SELECT Currency FROM Rates GROUP BY Currency HAVING COUNT(*) > 1", nativeQuery = true)
	List<String> findCountryHavingMoreThanOneCurrency();
}
