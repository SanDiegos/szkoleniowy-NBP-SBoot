package com.djedra.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.djedra.calculations.Calculations;
import com.djedra.entity.currency.Currency;
import com.djedra.service.currency.CurrencyService;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;
import com.djedra.util.EnumUtil;

@Component
public class CurrencyFacade {

	@Autowired
	private CurrencyService currencyService;
	
	@Valid
	public Currency getExchangeRateForDate(String tableType, String currencyCode,
			  @PastOrPresent LocalDate date) {
		ActualExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ActualExchangeRateTableTypes.class);
		CurrencyCode currCode = EnumUtil.getEnumByValue("currencyCode", currencyCode, CurrencyCode.class);
		return currencyService.getExchangeRateForDate(tabType, currCode, date);
	}

	public Currency getCurrentExchangeRate(String tableType, String currencyCode) {

		ActualExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ActualExchangeRateTableTypes.class);
		CurrencyCode currCode = EnumUtil.getEnumByValue("currencyCode", currencyCode, CurrencyCode.class);
		return currencyService.getCurrentExchangeRate(tabType, currCode);
	}

//	public Currency getExchangeRateFromFile(String patch) {
//		return nbpController.getExchangeRateFromFile(patch);
//	}

	public BigDecimal exchange(String tableType, String currencyCode, BigDecimal amount) {

		ActualExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ActualExchangeRateTableTypes.class);
		CurrencyCode currCode = EnumUtil.getEnumByValue("currencyCode", currencyCode, CurrencyCode.class);
		
		Currency curr = currencyService.getCurrentExchangeRate(tabType, currCode);
		if (Objects.isNull(curr) || Objects.isNull(curr.getRate())) {
			throw new RuntimeException("Didn't found current course currency.");
		}
		return Calculations.exchange(amount, curr.getRate());
	}
}
