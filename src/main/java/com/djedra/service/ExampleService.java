package com.djedra.service;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.djedra.connection.ExchangeRateURLEnhancer;
import com.djedra.entity.tableType.Example;
import com.djedra.exception.ConnectionException;
import com.djedra.repository.IExampleRepository;
import com.djedra.util.Constants.ExchangeRatesTableTypes;

@Service
public class ExampleService {

	@Autowired
	private IExampleRepository iExampleRepository;

	public Example getCurrentExchangeRates(ExchangeRatesTableTypes tableType) {
		Example example = iExampleRepository.findBytableType(tableType.getValue());
		return Objects.nonNull(example) ? example : makeRequestToNBP(new ExchangeRateURLEnhancer(tableType).getPath());
	}

	private Example makeRequestToNBP(URL url) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			return restTemplate.getForObject(url.toURI(), Example.class);
		} catch (RestClientException e) {
			throw new ConnectionException("Błąd połączenia z zewnętrznym API", e);
		} catch (URISyntaxException e) {
			throw new ConnectionException(String.format("Błędna składnia URI: [%s]", url.toString()), e);
		}
	}

}
