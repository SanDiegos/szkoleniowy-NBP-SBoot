package com.djedra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.djedra.entity.Currency;

public interface ICurrencyRepository extends JpaRepository<Currency, Long> {

	public Currency findByCodeAndRates_Date(String currencyCode, LocalDate date);

	public Currency findByCode(String currencyCode);

	@Query(value = "SELECT MAX(Rate.mid) - MIN(Rate.mid) as diffrence, Currency.Code FROM Rate inner join Currency on Currency.ID = Rate.Currency_Id group by Currency.Code ORDER BY diffrence DESC LIMIT 1", nativeQuery = true)
	List<Object[]> findHighestCurrencyCourseDeffrenceBetweenDates();

}
