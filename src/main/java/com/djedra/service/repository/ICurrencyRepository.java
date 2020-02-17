package com.djedra.service.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.currency.Currency;

public interface ICurrencyRepository extends JpaRepository<Currency, Integer>{

	public Currency findCurrencyByTableAndCodeAndRate_Rates(String tableType, String currencyCode,
			LocalDate date);
	
	public Currency findCurrencyByTableAndCode(String tableType, String currencyCode);
}