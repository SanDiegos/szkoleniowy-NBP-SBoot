package com.djedra.szkoleniowyNBPSBoot.controller;

import java.time.LocalDate;
import java.util.List;

import com.djedra.entity.currency.Currency;
import com.djedra.entity.currency.Rate;

public class CurrencyDataFactory {

	public static LocalDate VALID_EFFECTIVE_DATA = LocalDate.of(2020, 02, 02);
	
	public static Currency createValidData(List<Rate> rateList) {
		Currency currency = new Currency();
		currency.setId(1L);
		currency.setTableType("A");
		currency.setCurrency("currency");
		currency.setCode("code");
		currency.setRates(rateList);
		return currency;
	}
}
