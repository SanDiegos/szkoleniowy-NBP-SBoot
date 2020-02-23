package com.djedra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.djedra.entity.Country;
import com.djedra.entity.CurrencyToCountry;

public interface ICurrencyToCountryRepository extends JpaRepository<CurrencyToCountry, Long> {

	public List<CurrencyToCountry> findByCountry_Id(Long countryId);

	@Query(value = "SELECT c.country FROM CurrencyToCountry c GROUP BY c.country HAVING COUNT(*) > 1")
	List<Country> findCountryHavingMoreThanOneCurrency();
}
