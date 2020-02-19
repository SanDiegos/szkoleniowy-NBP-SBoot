package com.djedra.repository.currency;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.currency.Currency;

public interface ICurrencyRepository extends JpaRepository<Currency, Long> {

	public Currency findCurrencyBytableTypeAndCodeAndRates_effectiveDate(String tableType, String currencyCode,
			LocalDate effectiveDate);

	public List<Currency> findCurrencyBytableTypeAndCode(String tableType, String currencyCode);
}