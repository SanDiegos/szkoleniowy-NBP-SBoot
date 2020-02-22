package com.djedra.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.Currency;

public interface ICurrencyRepository extends JpaRepository<Currency, Long> {

	public Currency findByCodeAndRates_Date(String currencyCode, LocalDate date);

	public Currency findByCode(String currencyCode);

//
//	public List<Currency> findCurrencyByCode(String currencyCode);
}
