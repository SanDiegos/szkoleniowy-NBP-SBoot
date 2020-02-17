package com.djedra.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.currency.Currency;

public interface ICurrencyRepository extends JpaRepository<Currency, Long> {

	public Currency findCurrencyBytableTypeAndCodeAndRates_effectiveDate(String tableType, String currencyCode,
			LocalDate date);

	public Currency findCurrencyBytableTypeAndCode(String tableType, String currencyCode);
}