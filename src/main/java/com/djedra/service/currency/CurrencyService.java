package com.djedra.service.currency;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.Currency;
import com.djedra.nbpjsontopojo.currency.NBPCurrencyPOJO;
import com.djedra.parser.IParser;
import com.djedra.repository.ICountryRepository;
import com.djedra.repository.ICurrencyRepository;
import com.djedra.repository.ICurrencyToCountryRepository;
import com.djedra.repository.IRateRepository;
import com.djedra.util.Constants;
import com.djedra.util.Constants.CurrencyNBPAPIParamsKey;
import com.djedra.util.Constants.ExchangeRateTableTypes;

@Service
public class CurrencyService {

	private ICurrencyRepository currencyRepository;
	private IDataProvider<NBPCurrencyPOJO> dataProvider;
	private IParser<NBPCurrencyPOJO, Currency> parser;
	private ICountryRepository countryRepository;
	private ICurrencyToCountryRepository currencyToRateRepository;
	private IRateRepository rateRepository;

	@Autowired
	public CurrencyService(ICurrencyRepository currencyRepository, ICountryRepository countryRepository,
			ICurrencyToCountryRepository currencyToRateRepository, IRateRepository rateRepository,
			IDataProvider<NBPCurrencyPOJO> dataProvider, IParser<NBPCurrencyPOJO, Currency> parser) {
		this.currencyRepository = currencyRepository;
		this.dataProvider = dataProvider;
		this.parser = parser;
		this.countryRepository = countryRepository;
		this.currencyToRateRepository = currencyToRateRepository;
		this.rateRepository = rateRepository;
	}

	public Currency getExchangeRateForDate(ExchangeRateTableTypes tableType, String currencyCode, LocalDate date) {
		Currency currency = currencyRepository.findByCodeAndRates_Date(currencyCode, date);
		if (Objects.isNull(currency)) {
			currency = reciveDataFromNBP(tableType, currencyCode, date);
			currency = currencyRepository.save(currency);
			countryRepository.save(currency.getCurrencyToCountry().get(0).getCountry());
			rateRepository.saveAll(currency.getRates());
			currencyToRateRepository.saveAll(currency.getCurrencyToCountry());
		}
		return currency;
	}

	@Transactional
	private Currency reciveDataFromNBP(ExchangeRateTableTypes tableType, String currencyCode, LocalDate date) {
		Currency currency = null;
		int loop = Constants.NUMBER_OF_REPEATINGS_IN_SEARCH_FOR_DAY;
		HashMap<String, Object> paramsForNBPAPIDataProvider = prepareParamsForNBPAPIDataProvider(tableType,
				currencyCode, date);
		while (!dataProvider.hasData(paramsForNBPAPIDataProvider) && loop > 0) {
			date = date.minusDays(1);
			paramsForNBPAPIDataProvider.put(CurrencyNBPAPIParamsKey.DATE.getParamName(), date);
			--loop;
		}
		NBPCurrencyPOJO nbpCurrencyPOJO = dataProvider.downloadData(paramsForNBPAPIDataProvider);
		if (Objects.nonNull(nbpCurrencyPOJO) && Objects.nonNull(nbpCurrencyPOJO.getRates())
				&& nbpCurrencyPOJO.getRates().size() == 1) {
			currency = parser.parse(nbpCurrencyPOJO);

		}
		return currency;
	}

	public Currency getCurrentExchangeRate(ExchangeRateTableTypes tableType, String currencyCode) {
		Currency currency = currencyRepository.findByCodeAndRates_Date(currencyCode, LocalDate.now());
		if (Objects.isNull(currency)) {
			currency = reciveDataFromNBP(tableType, currencyCode, null);
		}
		return currency;
	}

	private HashMap<String, Object> prepareParamsForNBPAPIDataProvider(ExchangeRateTableTypes tableType,
			String currencyCode, LocalDate date) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(CurrencyNBPAPIParamsKey.TABLE_TYPE.getParamName(), tableType);
		params.put(CurrencyNBPAPIParamsKey.CURRENCY_CODE.getParamName(), currencyCode);
		if (Objects.nonNull(date)) {
			params.put(CurrencyNBPAPIParamsKey.DATE.getParamName(), date);
		}
		return params;
	}

	public void delete(Long currency_Id) {
		currencyRepository.deleteById(currency_Id);
	}

	public Currency save(Currency currency) {
		return currencyRepository.save(currency);
	}

	public Currency getById(Long currency_Id) {
		return currencyRepository.findById(currency_Id).orElseThrow(
				() -> new RuntimeException(String.format("Didn't find currency having Id: [%d]", currency_Id)));
	}

}
