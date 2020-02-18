package com.djedra.service;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.djedra.connection.ExchangeRateURLEnhancer;
import com.djedra.entity.currency.Currency;
import com.djedra.entity.currency.Rate;
import com.djedra.exception.ConnectionException;
import com.djedra.repository.ICurrencyRepository;
import com.djedra.repository.IRateRepository;
import com.djedra.util.Constants;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;

@Service
public class CurrencyService {

	private ICurrencyRepository currencyRepository;
	private IRateRepository rateRepository;

	@Autowired
	public CurrencyService(ICurrencyRepository currencyRepository, IRateRepository rateRepository) {
		this.currencyRepository = currencyRepository;
		this.rateRepository = rateRepository;
	}

	public Currency getExchangeRateForDate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode,
			LocalDate date) {

		Currency currency = null;
		int loop = Constants.NUMBER_OF_REPEATINGS_IN_SEARCH_FOR_DAY;
		while (Objects.isNull(currency) && loop > 0) {
			currency = currencyRepository.findCurrencyBytableTypeAndCodeAndRates_effectiveDate(tableType.getValue(),
					currencyCode.getCurrencyCode(), date);
			if (Objects.isNull(currency)) {
				URL url = new ExchangeRateURLEnhancer(tableType, currencyCode, date).getPath();
				currency = makeRequestToNBP(url);
				Rate rate = currency.getRates().get(0);
				rate.setCurrency(currency);
				rateRepository.save(rate);

				date = date.minusDays(1);
				--loop;
			}
		}
		return currency;
	}

	public Currency getCurrentExchangeRate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode) {
		Currency currency = currencyRepository.findCurrencyBytableTypeAndCode(tableType.getValue(),
				currencyCode.getCurrencyCode());
		return Objects.nonNull(currency) ? currency
				: makeRequestToNBP(new ExchangeRateURLEnhancer(tableType, currencyCode).getPath());
	}

	private Currency makeRequestToNBP(URL url) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			return restTemplate.getForObject(url.toURI(), Currency.class);
		} catch (RestClientException e) {
			throw new ConnectionException("Błąd połączenia z zewnętrznym API", e);
		} catch (URISyntaxException e) {
			throw new ConnectionException(String.format("Błędna składnia URI: [%s]", url.toString()), e);
		}
	}
//	public Example getCurrentExchangeRates(ExchangeRatesTableTypes tableType) {
//
//		HTTPConnection connection = new HTTPConnection(
//				new ExchangeRateURLEnhancer(NBPBaseURL.EXCHANGE_RATES_TABLE, tableType, null, null),
//				t -> HTTPConnectionValidators.validateConnection(t));
//		connection.validateConnection();
//		return currencyRepository.makeRequest(new HTTPtoExampleParser(), connection);
//	}

//	public Currency getExchangeRateFromFile(IPath<String> path) {
//		FileConnection connection = new FileConnection(path);
//		connection.validateConnection();
//		return currencyRepository.makeRequest(new FileToCurrencyParser(), new FileConnection(path));
//	}

//	public <S, D, P> D getCurrentExchangeRate(IDownloader<S> downloader, IParser<S, D> parser, IPath<P> path) {
//
//		HTTPConnection connection = new HTTPConnection(path, t -> HTTPConnectionValidators.validateConnection(t));
//		connection.validateConnection();
//		return parser.parse(downloader.download((IConnection<S>) connection));
//	}

}
