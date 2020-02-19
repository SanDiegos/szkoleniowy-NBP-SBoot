package com.djedra.repository.exchangeratestable;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.exchangeratestable.ExchangeRatesTable;

public interface IExchangeRatesTableRepository  extends JpaRepository<ExchangeRatesTable, Long> {

	public List<ExchangeRatesTable> findByEffectiveDateBetween(LocalDate dateFrom, LocalDate dateTo);
	
	ExchangeRatesTable findFirstByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(String code, LocalDate dateFrom, LocalDate dateTo);
	
	List<ExchangeRatesTable> findAllByeffectiveDateBetween(LocalDate dateFrom, LocalDate dateTo);
	
}
