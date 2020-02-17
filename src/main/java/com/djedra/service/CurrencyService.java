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
import com.djedra.exception.ConnectionException;
import com.djedra.service.repository.ICurrencyRepository;
import com.djedra.util.Constants;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;

@Service
public class CurrencyService {

	@Autowired
	private ICurrencyRepository currencyRepository;
	@Autowired
	private RestTemplate restTemplate;
	
	public Currency getExchangeRateForDate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode,
			LocalDate date) {

		Currency currency = null;
		int loop = Constants.NUMBER_OF_REPEATINGS_IN_SEARCH_FOR_DAY;
		while(Objects.isNull(currency)  && loop > 0){
//			restTemplate.getForEntity(new ExchangeRateURLEnhancer(NBPBaseURL.EXCHANGE_RATE_DATE, tableType, currencyCode, date).getPath(), Currency.class);
			
			currency = currencyRepository.findCurrencyByTableAndCodeAndRate_Rates(tableType.getValue(), currencyCode.getCurrencyCode(), date);
//			if(Objects.isNull(currency)) {
				URL url = new ExchangeRateURLEnhancer(tableType, currencyCode, date).getPath();
				currency = makeRequestToNBP(url);
				date = date.minusDays(1);
				--loop;
//			}
		}
		
//		if(Objects.isNull(currency)){
//			strzel_do_NBP_API
//		}
		
//		if(zwrocone_z_api null){
//          obniz date
//			potarzaj az znajdziesz
			
//		}else(){
//			update na bazie danych i zwrovcenie
//		}
		return currency;
	}

	public Currency getCurrentExchangeRate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode) {	
		Currency currency = currencyRepository.findCurrencyByTableAndCode(tableType.getValue(), currencyCode.getCurrencyCode());
		return Objects.nonNull(currency) ? currency : makeRequestToNBP(new ExchangeRateURLEnhancer(tableType,currencyCode).getPath());
	}

	private Currency makeRequestToNBP(URL url) {
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
