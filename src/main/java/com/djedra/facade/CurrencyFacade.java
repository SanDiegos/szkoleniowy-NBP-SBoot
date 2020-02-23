package com.djedra.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.djedra.calculations.Calculations;
import com.djedra.controller.ControllerArgumentsValidator;
import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.Rate;
import com.djedra.service.currency.CurrencyService;
import com.djedra.service.exchangeratestable.ExchangeRatesService;
import com.djedra.util.Constants.ExchangeRateTableTypes;
import com.djedra.util.Constants.ExchangeRatesTableTypes;
import com.djedra.util.EnumUtil;

@Component
public class CurrencyFacade {

	private CurrencyService currencyService;
	private ExchangeRatesService exchangeRatesTableService;

	@Autowired
	public CurrencyFacade(CurrencyService currencyService, ExchangeRatesService exchangeRatesTableService) {
		this.currencyService = currencyService;
		this.exchangeRatesTableService = exchangeRatesTableService;
	}

	@Valid
	public Currency getExchangeRateForDate(String tableType, String currencyCode, @PastOrPresent LocalDate date) {
		ExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType, ExchangeRateTableTypes.class);
		return currencyService.getExchangeRateForDate(tabType, currencyCode, date);
	}

	public Currency getCurrentExchangeRate(String tableType, String currencyCode) {

		ExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType, ExchangeRateTableTypes.class);
		return currencyService.getCurrentExchangeRate(tabType, currencyCode);
	}

	public BigDecimal exchange(String tableType, String currencyCode, BigDecimal amount) {

		ExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType, ExchangeRateTableTypes.class);
		Currency curr = currencyService.getCurrentExchangeRate(tabType, currencyCode);
		if (Objects.isNull(curr) || Objects.isNull(curr.getRates())) {
			throw new RuntimeException("Didn't found current course currency.");
		}
		return Calculations.exchange(amount, curr.getRate());
	}

	public Rate getHighestCurrencyCourseBetweenDates(String currencyCode, LocalDate dateFrom, LocalDate dateTo) {
		ControllerArgumentsValidator.checkIfDateFromIsBeforeDateTo(dateFrom, dateTo);
		return exchangeRatesTableService.getHighestExchangeRatesTable(currencyCode, dateFrom, dateTo);
	}

	public String getCurrencyWithHighestCourseDiffrenceBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
		ControllerArgumentsValidator.checkIfDateFromIsBeforeDateTo(dateFrom, dateTo);
		return exchangeRatesTableService.getCurrencyWithHighestDeffrenceBetweenDates(dateFrom, dateTo);
	}

	public List<Country> getCountryHavingMoreThanOneCurrency() {
		return exchangeRatesTableService.getCountryHavingMoreThanOneCurrency();
	}

	public Currency save(Currency currency) {
		return currencyService.save(currency);
	}

	public void delete(Long currency_Id) {
		currencyService.delete(currency_Id);
	}

	public Currency getById(Long currency_Id) {
		return currencyService.getById(currency_Id);
	}

	public List<Rate> getFiveHighestOrLowestCurrencyCourse(String tableType, String currencyCode, LocalDate dateFrom,
			LocalDate dateTo, Boolean topHigh) {
		ControllerArgumentsValidator.checkIfDateFromIsBeforeDateTo(dateFrom, dateTo);
		ExchangeRatesTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ExchangeRatesTableTypes.class);
		if (Objects.isNull(topHigh)) {
			throw new RuntimeException("U must define which top 5 rates you need low or high.");
		}
		return exchangeRatesTableService.getFiveHighestOrLowestCurrencyCourse(tabType, currencyCode, dateFrom, dateTo,
				topHigh.booleanValue());
	}

}
