package com.djedra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.djedra.entity.Currency;

public interface ICurrencyRepository extends JpaRepository<Currency, Long> {

	public Currency findByCodeAndRates_Date(String currencyCode, LocalDate date);

	public Currency findByCode(String currencyCode);

//	public List<ExchangeRatesTable> findByEffectiveDateBetween(LocalDate dateFrom, LocalDate dateTo);

//	Currency findFirstByCodeAndRate_DateBetweenOrderByRate_MidDesc(String code, LocalDate dateFrom, LocalDate dateTo);

//	Currency findFirstBycodeAndRate_dateBetweenOrderByRate_midDesc(String code, LocalDate dateFrom, LocalDate dateTo);

//	List<Currency> findAllByeffectiveDateBetween(LocalDate dateFrom, LocalDate dateTo);

	@Query(value = "SELECT MAX(Rate.mid) - MIN(Rate.mid) as diffrence, Currency.Code FROM Rate inner join Currency on Currency.ID = Rate.Currency_Id group by Currency.Code ORDER BY diffrence DESC LIMIT 1", nativeQuery = true)
	List<Object[]> findHighestCurrencyCourseDeffrenceBetweenDates();

//	from DomesticCat as cat
//	where cat.name = some (
//	    select name.nickName from Name as name
//	)
}
