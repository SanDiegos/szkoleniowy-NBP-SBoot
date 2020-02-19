package com.djedra.service.exchangeratestable;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.djedra.entity.exchangeratestable.ExchangeRatesTable;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;

@Service
public class ExchangeRatesTableService {

	public ExchangeRatesTable getExchangeRatesTable(ActualExchangeRateTableTypes tableType, LocalDate dateFrom, LocalDate dateTo) {
		
		return new ExchangeRatesTable();
	}
}
