package com.djedra.service;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.currency.Currency;
import com.djedra.entity.currency.Rate;
import com.djedra.repository.ICurrencyRepository;
import com.djedra.repository.IRateRepository;
import com.djedra.util.Constants;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;

@Service
public class CurrencyService {

	private ICurrencyRepository currencyRepository;
	private IRateRepository rateRepository;
	private IDataProvider<Currency> dataProvider;

	@Autowired
	public CurrencyService(ICurrencyRepository currencyRepository, IRateRepository rateRepository,
			IDataProvider<Currency> dataProvider) {
		this.currencyRepository = currencyRepository;
		this.rateRepository = rateRepository;
		this.dataProvider = dataProvider;
	}

	public Currency getExchangeRateForDate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode,
			LocalDate date) {

		Currency currency = currencyRepository.findCurrencyBytableTypeAndCodeAndRates_effectiveDate(
				tableType.getValue(), currencyCode.getCurrencyCode(), date);
		if (Objects.isNull(currency)) {
			int loop = Constants.NUMBER_OF_REPEATINGS_IN_SEARCH_FOR_DAY;
			while (!dataProvider.hasData(tableType, currencyCode, date) && loop > 0) {
				date = date.minusDays(1);
				--loop;
			}
			currency = dataProvider.downloadData(tableType, currencyCode, date);
//				URL url = new ExchangeRateURLEnhancer(tableType, currencyCode, date).getPath();
//				currency = makeRequestToNBP(url);
			Rate rate = currency.getRates().get(0);
			rate.setCurrency(currency);
			rateRepository.save(rate);
		}
		return currency;
	}

	public Currency getCurrentExchangeRate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode) {
		Currency currency = currencyRepository.findCurrencyBytableTypeAndCode(tableType.getValue(),
				currencyCode.getCurrencyCode());
		return Objects.nonNull(currency) ? currency : dataProvider.downloadData(tableType, currencyCode, null);
	}

//	private Currency makeRequestToNBP(URL url) {
//		RestTemplate restTemplate = new RestTemplate();
//		try {
//			return restTemplate.getForObject(url.toURI(), Currency.class);
//		} catch (RestClientException e) {
//			throw new ConnectionException("Błąd połączenia z zewnętrznym API", e);
//		} catch (URISyntaxException e) {
//			throw new ConnectionException(String.format("Błędna składnia URI: [%s]", url.toString()), e);
//		}
//	}
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
