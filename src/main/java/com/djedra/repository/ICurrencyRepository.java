package com.djedra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.djedra.entity.Currency;
import com.djedra.entity.ICurrecnyDiffResponse;

public interface ICurrencyRepository extends JpaRepository<Currency, Long> {

	public Currency findByCodeAndRates_Date(String currencyCode, LocalDate date);

	public Currency findByCode(String currencyCode);

	@Query(value = "SELECT MAX(Rate.mid) - MIN(Rate.mid) AS diffrence, Currency.Code FROM Rate INNER JOIN Currency ON Currency.Id = Rate.Currency_Id GROUP BY Currency.Code ORDER BY diffrence DESC LIMIT 1", nativeQuery = true)
	List<Object[]> findHighestCurrencyCourseDeffrenceBetweenDates();

	@Query(value = "SELECT * FROM currency cur INNER JOIN (SELECT ra.currency_Id, (MAX(ra.mid) - MIN(ra.mid)) AS diff FROM rate ra INNER JOIN currency cu ON cu.id = ra.currency_id WHERE ra.date BETWEEN '2020-02-01' AND '2020-02-15' GROUP BY cu.code,  ra.currency_Id ORDER BY diff DESC LIMIT 1) rate ON cur.id = rate.currency_id", nativeQuery = true)
	ICurrecnyDiffResponse findHighestCurrencyCourseDeffrenceBetweenDatesObj();

//	@Query(value = "FROM currency AS curr, (SELECT ra.currency_Id, (MAX(ra.mid) - MIN(ra.mid)) AS diff FROM rate ra INNER JOIN ra.currency cu WHERE ra.date BETWEEN '2020-02-01' AND '2020-02-15' GROUP BY cu.code, ra.currency_Id ORDER BY diff DESC LIMIT 1) AS rate INNER JOIN rate.currency curr")
//	ICurrecnyDiffResponse findHighestCurrencyCourseDeffrenceBetweenDates();
}
