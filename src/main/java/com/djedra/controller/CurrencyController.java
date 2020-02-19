package com.djedra.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djedra.entity.currency.Currency;
import com.djedra.facade.CurrencyFacade;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyController {

	@Autowired
	private CurrencyFacade currencyFacade;

	@Valid
	@GetMapping("/get-by-date")
	public Currency getExchangeRateForDate(@RequestParam String tableType, @RequestParam String currencyCode,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) /* @PastOrPresent */ LocalDate date) {
//		ControllerArgumentsValidator.checkIfDateIsPastOrPresent(date);
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

}
