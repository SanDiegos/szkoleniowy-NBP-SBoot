package com.djedra.connection;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.djedra.entity.currency.Currency;
import com.djedra.exception.ConnectionException;
import com.djedra.util.Constants.CurrencyNBPAPIParamsKey;
import com.djedra.util.Constants.ExchangeRateTableTypes;

@Component
public class NBPDataProviderCurrency implements IDataProvider<Currency> {

	@Override
	public Currency downloadData(HashMap<String, Object> params) {
		RestTemplate restTemplate = new RestTemplate();
		
		LocalDate date = (LocalDate) params.get(CurrencyNBPAPIParamsKey.DATE.getParamName());
		ExchangeRateTableTypes tableType = (ExchangeRateTableTypes) params.get(CurrencyNBPAPIParamsKey.TABLE_TYPE.getParamName());
		String currencyCode = (String) params.get(CurrencyNBPAPIParamsKey.CURRENCY_CODE.getParamName());
		URL path = Objects.isNull(date) ? new ExchangeRateURLEnhancer(tableType, currencyCode).getPath() : new ExchangeRateURLEnhancer(tableType, currencyCode, date).getPath();
		try {
			return restTemplate.getForObject(path.toURI(), Currency.class);
		} catch (RestClientException e) {
			throw new ConnectionException("Błąd połączenia z zewnętrznym API", e);
		} catch (URISyntaxException e) {
			throw new ConnectionException(String.format("Błędna składnia URI: [%s]", path.toString()), e);
		}
	}

	@Override
	public boolean hasData(HashMap<String, Object> params) {
		return Objects.nonNull(downloadData(params));
	}
}
