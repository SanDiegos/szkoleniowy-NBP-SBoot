package com.djedra.service.currency;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.currency.Currency;
import com.djedra.entity.currency.Rate;
import com.djedra.repository.currency.ICurrencyRepository;
import com.djedra.repository.currency.IRateRepository;
import com.djedra.util.Constants;
import com.djedra.util.Constants.CurrencyNBPAPIParamsKey;
import com.djedra.util.Constants.ExchangeRateTableTypes;

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

	public Currency getExchangeRateForDate(ExchangeRateTableTypes tableType, String currencyCode,
			LocalDate date) {

		Currency currency = currencyRepository.findCurrencyBytableTypeAndCodeAndRates_effectiveDate(
				tableType.getValue(), currencyCode, date);
		if (Objects.isNull(currency)) {
			int loop = Constants.NUMBER_OF_REPEATINGS_IN_SEARCH_FOR_DAY;
			HashMap<String, Object> paramsForNBPAPIDataProvider = prepareParamsForNBPAPIDataProvider(tableType, currencyCode, date);
			while (!dataProvider.hasData(paramsForNBPAPIDataProvider) && loop > 0) {
				date = date.minusDays(1);
				paramsForNBPAPIDataProvider.put(CurrencyNBPAPIParamsKey.DATE.getParamName(), date);
				--loop;
			}
			currency = dataProvider.downloadData(paramsForNBPAPIDataProvider);
			Rate rate = currency.getRates().get(0);
			rate.setCurrency(currency);
			rateRepository.save(rate);
		}
		return currency;
	}

	public Currency getCurrentExchangeRate(ExchangeRateTableTypes tableType, String currencyCode) {
		Currency currency = currencyRepository.findCurrencyBytableTypeAndCodeAndRates_effectiveDate(
				tableType.getValue(), currencyCode, LocalDate.now());
		return Objects.nonNull(currency) ? currency : dataProvider.downloadData(prepareParamsForNBPAPIDataProvider(tableType, currencyCode, null));
	}
	
	private HashMap<String, Object> prepareParamsForNBPAPIDataProvider(ExchangeRateTableTypes tableType, String currencyCode,
			LocalDate date){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(CurrencyNBPAPIParamsKey.TABLE_TYPE.getParamName(), tableType);
		params.put(CurrencyNBPAPIParamsKey.CURRENCY_CODE.getParamName(), currencyCode);
		if(Objects.nonNull(date)) {
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
		return currencyRepository.findById(currency_Id).orElseThrow(() -> new RuntimeException(String.format("Didn't find currency having Id: [%d]", currency_Id)));
	}
	 
//	public List<Currency> getCurrentExchangeRate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode) {
//		List<Currency> currency = currencyRepository.findAllCurrencyBytableTypeAndCode(tableType.getValue(),
//				currencyCode.getCurrencyCode());
//		return currency;
//	}

}
