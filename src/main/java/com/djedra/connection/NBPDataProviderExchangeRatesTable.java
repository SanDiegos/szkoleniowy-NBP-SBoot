package com.djedra.connection;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.djedra.entity.exchangeratestable.ExchangeRatesTable;
import com.djedra.exception.ConnectionException;
import com.djedra.util.Constants.ExchangeRatesTableNBPAPIParamsKey;

@Component
public class NBPDataProviderExchangeRatesTable  implements IDataProvider<ExchangeRatesTable[]> {

	@Override
	public ExchangeRatesTable[] downloadData(HashMap<String, Object> params) {
		RestTemplate restTemplate = new RestTemplate();
		LocalDate dateFrom = (LocalDate) params.get(ExchangeRatesTableNBPAPIParamsKey.DATE_FROM.getParamName());
		LocalDate dateTo = (LocalDate) params.get(ExchangeRatesTableNBPAPIParamsKey.DATE_TO.getParamName());
		String tableType = (String) params.get(ExchangeRatesTableNBPAPIParamsKey.TABLE_TYPE.getParamName());
		
		URL path = new ExchangeRateURLEnhancer(tableType, dateFrom, dateTo).getPath();
		try {
			return restTemplate.getForObject(path.toURI(), ExchangeRatesTable[].class);
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
