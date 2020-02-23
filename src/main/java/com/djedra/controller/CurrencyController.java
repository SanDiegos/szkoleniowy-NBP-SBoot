package com.djedra.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.Rate;
import com.djedra.facade.CurrencyFacade;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyController {

	@Autowired
	private CurrencyFacade currencyFacade;

	@Valid
	@GetMapping("/get-by-date")
	public Currency getExchangeRateForDate(@RequestParam String tableType, @RequestParam String currencyCode,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date) {
		ControllerArgumentsValidator.checkIfDateIsPastOrPresent(date);
		return currencyFacade.getExchangeRateForDate(tableType, currencyCode, date);
	}

	@GetMapping("/exchange")
	public BigDecimal exchange(@RequestParam String tableType, @RequestParam String currencyCode,
			@RequestParam BigDecimal amount) {
		return currencyFacade.exchange(tableType, currencyCode, amount);
	}

	@GetMapping("/get-currency-course")
	public Currency getByCurrencyCode(@RequestParam String tableType, @RequestParam String currencyCode)
			throws Exception {
		return currencyFacade.getCurrentExchangeRate(tableType, currencyCode);
	}

	@GetMapping("/get-highest_currency-course_between_dates")
	public Rate getHighestCurrencyCourseBetweenDates(@RequestParam String currencyCode,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) throws Exception {
		return currencyFacade.getHighestCurrencyCourseBetweenDates(currencyCode, dateFrom, dateTo);
	}

	@GetMapping("/get-currency-highest-course-diffrence-between-dates")
	public String getCurrencyWithHighestCourseDiffrenceBetweenDates(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) throws Exception {
		return currencyFacade.getCurrencyWithHighestCourseDiffrenceBetweenDates(dateFrom, dateTo);
	}

	@GetMapping("/get-country-having-more-than-one-currency")
	public List<Country> getCountryHavingMoreThanOneCurrency() throws Exception {
		return currencyFacade.getCountryHavingMoreThanOneCurrency();
	}

//	tu dodać daty aby określić z jakiego okresu?
	@GetMapping("/get-five-highest-and-lowest-currency-courses")
	public List<Rate> getFiveHighestAndLowestCurrencyCourse(@RequestParam String tableType,
			@RequestParam String currencyCode,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
			@RequestParam Boolean topHigh) {
		return currencyFacade.getFiveHighestOrLowestCurrencyCourse(tableType, currencyCode, dateFrom, dateTo, topHigh);
	}

	@PostMapping()
	public Currency add(@RequestBody Currency currency) {
		return currencyFacade.save(currency);
	}

	@DeleteMapping
	public void delete(@PathVariable Long currency_Id) {
		currencyFacade.delete(currency_Id);
	}

	@GetMapping
	public Currency getById(@PathVariable Long currency_Id) {
		return currencyFacade.getById(currency_Id);
	}

}
