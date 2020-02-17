package com.djedra.service;

import java.time.LocalDate;

import com.djedra.connection.ExchangeRateURLEnhancer;
import com.djedra.connection.FileConnection;
import com.djedra.connection.HTTPConnection;
import com.djedra.connection.IPath;
import com.djedra.connection.validator.HTTPConnectionValidators;
import com.djedra.entity.currency.Currency;
import com.djedra.entity.tableType.Example;
import com.djedra.parser.FileToCurrencyParser;
import com.djedra.parser.HTTPtoExampleParser;
import com.djedra.parser.StringtoCurrencyParser;
import com.djedra.repository.CurrencyRepository;
import com.djedra.util.Constants;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;
import com.djedra.util.Constants.ExchangeRatesTableTypes;
import com.djedra.util.Constants.NBPBaseURL;

public class CurrencyService {

	private final CurrencyRepository currencyRepository = new CurrencyRepository();

	public Currency getExchangeRateForDate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode,
			LocalDate date) {

		HTTPConnection connection = new HTTPConnection(
				new ExchangeRateURLEnhancer(NBPBaseURL.EXCHANGE_RATE_DATE, tableType, currencyCode, date),
				t -> HTTPConnectionValidators.validateConnectionWithoutThrow(t));

		int loop = Constants.NUMBER_OF_REPEATINGS_IN_SEARCH_FOR_DAY;
		while (!connection.validateConnection() && loop > 0) {
			date = date.minusDays(1);
			--loop;
			connection = new HTTPConnection(
					new ExchangeRateURLEnhancer(NBPBaseURL.EXCHANGE_RATE_DATE, tableType, currencyCode, date),
					t -> HTTPConnectionValidators.validateConnectionWithoutThrow(t));
		}

		return currencyRepository.makeRequest(new StringtoCurrencyParser(), connection);
	}

	public Currency getCurrentExchangeRate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode) {

		HTTPConnection connection = new HTTPConnection(
				new ExchangeRateURLEnhancer(NBPBaseURL.EXCHANGE_RATE, tableType, currencyCode, null),
				t -> HTTPConnectionValidators.validateConnection(t));
		connection.validateConnection();
		return currencyRepository.makeRequest(new StringtoCurrencyParser(), connection);
	}

	public Example getCurrentExchangeRates(ExchangeRatesTableTypes tableType) {

		HTTPConnection connection = new HTTPConnection(
				new ExchangeRateURLEnhancer(NBPBaseURL.EXCHANGE_RATES_TABLE, tableType, null, null),
				t -> HTTPConnectionValidators.validateConnection(t));
		connection.validateConnection();
		return currencyRepository.makeRequest(new HTTPtoExampleParser(), connection);
	}

	public Currency getExchangeRateFromFile(IPath<String> path) {
		FileConnection connection = new FileConnection(path);
		connection.validateConnection();
		return currencyRepository.makeRequest(new FileToCurrencyParser(), new FileConnection(path));
	}

//	public <S, D, P> D getCurrentExchangeRate(IDownloader<S> downloader, IParser<S, D> parser, IPath<P> path) {
//
//		HTTPConnection connection = new HTTPConnection(path, t -> HTTPConnectionValidators.validateConnection(t));
//		connection.validateConnection();
//		return parser.parse(downloader.download((IConnection<S>) connection));
//	}

}
