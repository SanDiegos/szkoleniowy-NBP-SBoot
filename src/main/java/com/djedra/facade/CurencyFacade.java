package com.djedra.facade;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.djedra.controller.CurrencyController;
import com.djedra.entity.currency.Currency;
import com.djedra.entity.tableType.Example;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;
import com.djedra.util.Constants.ExchangeRatesTableTypes;
import com.djedra.util.EnumUtil;

public class CurencyFacade {

	private final CurrencyController nbpController = new CurrencyController();

	public Currency getExchangeRateForDate(String tableType, String currencyCode,
			/* @Valid @PastOrPresent */ LocalDate date) {

		ActualExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ActualExchangeRateTableTypes.class);
		CurrencyCode currCode = EnumUtil.getEnumByValue("currencyCode", currencyCode, CurrencyCode.class);
		return nbpController.getExchangeRateForDate(tabType, currCode, date);
	}

	public Currency getCurrentExchangeRate(String tableType, String currencyCode) {

		ActualExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ActualExchangeRateTableTypes.class);
		CurrencyCode currCode = EnumUtil.getEnumByValue("currencyCode", currencyCode, CurrencyCode.class);
		return nbpController.getCurrentExchangeRate(tabType, currCode);
	}

	public Example getExchangeRates(String tableType) {

		ExchangeRatesTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ExchangeRatesTableTypes.class);
		return nbpController.getExchangeRatesTable(tabType);
	}

	public Currency getExchangeRateFromFile(String patch) {

		return nbpController.getExchangeRateFromFile(patch);
	}

	public BigDecimal exchange(String tableType, String currencyCode, BigDecimal amount) {

		ActualExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ActualExchangeRateTableTypes.class);
		CurrencyCode currCode = EnumUtil.getEnumByValue("currencyCode", currencyCode, CurrencyCode.class);
		return nbpController.exchange(tabType, currCode, amount);
	}

}
