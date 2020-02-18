package com.djedra.connection;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.djedra.entity.currency.Currency;
import com.djedra.exception.ConnectionException;
import com.djedra.util.Constants.ActualExchangeRateTableTypes;
import com.djedra.util.Constants.CurrencyCode;

@Component
public class NBPDataProvider implements IDataProvider<Currency> {

	public NBPDataProvider() {
	}

	@Override
	public Currency downloadData(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode, LocalDate date) {
		RestTemplate restTemplate = new RestTemplate();
		URL path = new ExchangeRateURLEnhancer(tableType, currencyCode, date).getPath();
		try {
			return restTemplate.getForObject(path.toURI(), Currency.class);
		} catch (RestClientException e) {
			throw new ConnectionException("Błąd połączenia z zewnętrznym API", e);
		} catch (URISyntaxException e) {
			throw new ConnectionException(String.format("Błędna składnia URI: [%s]", path.toString()), e);
		}
	}

	@Override
	public boolean hasData(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode, LocalDate date) {
		return Objects.nonNull(downloadData(tableType, currencyCode, date));
	}

//	private HttpURLConnection httpURLConnectionCreator(ActualExchangeRateTableTypes tableType,
//			CurrencyCode currencyCode, LocalDate date) {
//
//		URL url = Objects.isNull(date) ? new ExchangeRateURLEnhancer(tableType, currencyCode).getPath()
//				: new ExchangeRateURLEnhancer(tableType, currencyCode, date).getPath();
//		HttpURLConnection connection = null;
//		try {
//			connection = (HttpURLConnection) url.openConnection();
//		} catch (IOException e) {
//			throw new ConnectionException("Error while trying to open connection via HTTP.", e);
//		}
//		return connection;
//	}

}
