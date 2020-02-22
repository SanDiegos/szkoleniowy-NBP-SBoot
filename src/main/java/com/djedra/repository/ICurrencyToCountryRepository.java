package com.djedra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.CurrencyToCountry;

public interface ICurrencyToCountryRepository extends JpaRepository<CurrencyToCountry, Long> {

	public List<CurrencyToCountry> findByCountry_Id(Long countryId);
}
