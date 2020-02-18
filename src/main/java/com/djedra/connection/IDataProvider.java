package com.djedra.connection;

import java.time.LocalDate;

import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;

public interface IDataProvider<C> {

	C downloadData(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode, LocalDate date);

	boolean hasData(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode, LocalDate date);
}
