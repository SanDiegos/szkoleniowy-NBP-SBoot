package com.djedra.connection;

import java.time.LocalDate;

import com.djedra.entity.exchangeratestable.ExchangeRatesTable;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;

public class NBPDataProviderExchangeRatesTable  implements IDataProvider<ExchangeRatesTable> {

	@Override
	public ExchangeRatesTable downloadData(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode,
			LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasData(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode, LocalDate date) {
		// TODO Auto-generated method stub
		return false;
	}

}
