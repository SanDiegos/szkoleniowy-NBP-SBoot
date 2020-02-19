package com.djedra.repository.exchangeratestable;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.djedra.entity.exchangeratestable.ExchangeRatesTable;

public interface IExchangeRatesTableRepository extends JpaRepository<ExchangeRatesTable, Long> {

	public List<ExchangeRatesTable> findByEffectiveDateBetween(LocalDate dateFrom, LocalDate dateTo);

	ExchangeRatesTable findFirstByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(String code,
			LocalDate dateFrom, LocalDate dateTo);

	List<ExchangeRatesTable> findAllByeffectiveDateBetween(LocalDate dateFrom, LocalDate dateTo);

	@Query(value = "SELECT MAX(Rates.rate) - MIN(Rates.rate), rates.code FROM Rates inner join Exchange_Rates_Table on Exchange_Rates_Table.ID = Rates.ECHANGE_RATES_TABLE_ID group by rates.code", nativeQuery = true)
	List<Object[]> findHighestCurrencyCourseDeffrenceBetweenDates();

}
