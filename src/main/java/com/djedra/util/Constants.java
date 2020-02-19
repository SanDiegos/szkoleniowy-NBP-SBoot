package com.djedra.util;

import lombok.Getter;

public class Constants {

	public final static String FILE_PATH = "C:\\Users\\daniel.jedra\\Documents\\JSON.txt";
//	public final static String FILE_PATH = "E:JSON.txt";
	public final static int NUMBER_OF_REPEATINGS_IN_SEARCH_FOR_DAY = 15;

	@Getter
	public enum NBPBaseURL {

		EXCHANGE_RATE("http://api.nbp.pl/api/exchangerates/rates/%s/%s/"),
		EXCHANGE_RATE_DATE("http://api.nbp.pl/api/exchangerates/rates/%s/%s/%s/"),
		EXCHANGE_RATES_TABLE("http://api.nbp.pl/api/exchangerates/tables/%s/"),
		EXCHANGE_RATES_TABLE_DATE("http://api.nbp.pl/api/exchangerates/tables/%s/%s/%s/");
		

		private String url;

		NBPBaseURL(String url) {
			this.url = url;
		}
	}

	public enum ActualExchangeRateTableTypes implements IEnumType<String>, ITableType {
		A, C;

		@Override
		public String getValue() {
			return this.toString();
		}
	}

	public enum ExchangeRatesTableTypes implements IEnumType<String>, ITableType {
		A, B, C;

		@Override
		public String getValue() {
			return this.toString();
		}
	}

	@Getter
	public enum CurrencyCode implements IEnumType<String> {
		SWEDISH_KORONA("SEK"), SWISS_FRANC("CHF"), EURO("EUR"), US_DOLLAR("USD");

		private String currencyCode;

		CurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		@Override
		public String getValue() {
			return currencyCode;
		}
	}

	public enum httpResponseType {
		json, xml;
	}
}
