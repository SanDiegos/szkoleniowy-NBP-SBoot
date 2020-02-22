package com.djedra.szkoleniowyNBPSBoot.controller;

import java.time.LocalDate;
import java.util.List;

import com.djedra.entity.Currency;
import com.djedra.nbpjsontopojo.currency.NBPCurrencyRatePOJO;

public class CurrencyDataFactory {

	public static LocalDate VALID_EFFECTIVE_DATA = LocalDate.of(2020, 02, 02);

	public static Currency createValidData(List<NBPCurrencyRatePOJO> rateList) {
		Currency currency = new Currency("");
		currency.setId(1L);
		currency.setCode("code");
//		currency.setRates(rateList);
		return currency;
	}
}
