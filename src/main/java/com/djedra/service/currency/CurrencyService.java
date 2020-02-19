package com.djedra.service.currency;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.currency.Currency;
import com.djedra.entity.currency.Rate;
import com.djedra.repository.currency.ICurrencyRepository;
import com.djedra.repository.currency.IRateRepository;
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
			Rate rate = currency.getRates().get(0);
			rate.setCurrency(currency);
			rateRepository.save(rate);
		}
		return currency;
	}

	public Currency getCurrentExchangeRate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode) {
		Currency currency = currencyRepository.findCurrencyBytableTypeAndCodeAndRates_effectiveDate(
				tableType.getValue(), currencyCode.getCurrencyCode(), LocalDate.now());
		return Objects.nonNull(currency) ? currency : dataProvider.downloadData(tableType, currencyCode, null);
	}
	 
//	public List<Currency> getCurrentExchangeRate(ActualExchangeRateTableTypes tableType, CurrencyCode currencyCode) {
//		List<Currency> currency = currencyRepository.findAllCurrencyBytableTypeAndCode(tableType.getValue(),
//				currencyCode.getCurrencyCode());
//		return currency;
//	}


}
