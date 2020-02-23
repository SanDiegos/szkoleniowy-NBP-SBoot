package com.djedra.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.CurrencyToCountry;
import com.djedra.entity.Rate;

public class DataFactory {

	public static Currency getData(LocalDate date, BigDecimal mid, String countryName, String currencyCode) {
		List<Rate> rates = new ArrayList<Rate>();
		List<Country> countries = new ArrayList<Country>();
		Rate rate = new Rate(date, mid);
		rates.add(rate);
		countries.add(new Country(countryName));
		Currency currency = new Currency(currencyCode);
		rates.forEach(r -> r.setCurrency(currency));
		CurrencyToCountry currToCountry = new CurrencyToCountry();
		for (Country country : countries) {
			currToCountry.setCountry(country);
			currToCountry.setCurrency(currency);
		}
		currency.setRates(rates);
		currency.setCurrencyToRates(Arrays.asList(currToCountry));
		return currency;
	}

	public static List<Currency> getExampleData() {
		List<Currency> currencyList = new ArrayList<>();
		currencyList.add(DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(1), "Polska", "Zloty"));
		currencyList.add(DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(2), "Polska", "Zloty"));
		currencyList.add(DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(44), "Niemcy", "Euro"));
		currencyList.add(DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(55), "Niemcy", "Euro"));
		currencyList.add(DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(66), "Niemcy", "Marka"));
		return currencyList;
	}
}
