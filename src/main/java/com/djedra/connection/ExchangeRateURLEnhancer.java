package com.djedra.connection;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

import com.djedra.util.Constants.CurrencyCode;
import com.djedra.util.Constants.NBPBaseURL;
import com.djedra.util.ITableType;

import lombok.Getter;

@Getter
public class ExchangeRateURLEnhancer implements IHTTPConnectionURL {

	private URL path;

	public ExchangeRateURLEnhancer(ITableType tableType, CurrencyCode currencyCode, LocalDate date) {
		String urlAsString = createUrlAsString(NBPBaseURL.EXCHANGE_RATE_DATE, tableType, currencyCode, date);
		try {
			this.path = new URL(urlAsString);
		} catch (MalformedURLException e) {
			System.err.println(String.format("Error while creating URL from String: [%s]", urlAsString));
		}
	}

	public ExchangeRateURLEnhancer(ITableType tableType, CurrencyCode currencyCode) {
		String urlAsString = createUrlAsString(NBPBaseURL.EXCHANGE_RATE, tableType, currencyCode, null);
		try {
			this.path = new URL(urlAsString);
		} catch (MalformedURLException e) {
			System.err.println(String.format("Error while creating URL from String: [%s]", urlAsString));
		}
	}

	public ExchangeRateURLEnhancer(ITableType tableType) {
		String urlAsString = createUrlAsString(NBPBaseURL.EXCHANGE_RATES_TABLE, tableType, null, null);
		try {
			this.path = new URL(urlAsString);
		} catch (MalformedURLException e) {
			System.err.println(String.format("Error while creating URL from String: [%s]", urlAsString));
		}
	}

	private String createUrlAsString(NBPBaseURL baseUrl, ITableType tableType, CurrencyCode currencyCode,
			LocalDate date) {
		return String.format(baseUrl.getUrl(), tableType.toString(),
				Objects.isNull(currencyCode) ? "" : currencyCode.getCurrencyCode(),
				Objects.isNull(date) ? "" : date.toString());
	}

}
