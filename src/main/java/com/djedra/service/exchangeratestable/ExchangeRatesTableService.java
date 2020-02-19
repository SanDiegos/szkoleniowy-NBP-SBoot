package com.djedra.service.exchangeratestable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.exchangeratestable.ExchangeRatesTable;
import com.djedra.entity.exchangeratestable.Rates;
import com.djedra.repository.exchangeratestable.IExchangeRatesTableRepository;
import com.djedra.repository.exchangeratestable.IRatesRepository;
import com.djedra.util.Constants.ExchangeRatesTableNBPAPIParamsKey;

@Service
public class ExchangeRatesTableService {

	private IDataProvider<ExchangeRatesTable[]> dataProvider;
	private IExchangeRatesTableRepository iExchangeRatesTableRepository;
	private IRatesRepository iRatesRepository;

	@Autowired
	public ExchangeRatesTableService(IExchangeRatesTableRepository iExchangeRatesTableRepository,
			IRatesRepository iRatesRepository, IDataProvider<ExchangeRatesTable[]> dataProvider) {
		this.iExchangeRatesTableRepository = iExchangeRatesTableRepository;
		this.dataProvider = dataProvider;
		this.iRatesRepository = iRatesRepository;
	}

	public BigDecimal getHighestExchangeRatesTable(String currencyCode, LocalDate dateFrom, LocalDate dateTo) {
		uploadDataFromNBPToDB(dateFrom, dateTo);
		ExchangeRatesTable ratesTable = iExchangeRatesTableRepository
				.findFirstByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo);
		Optional<BigDecimal> findAny = ratesTable.getRates().stream().filter(r -> r.getCode().equals(currencyCode))
				.map(r -> r.getRate()).findAny();
		return findAny.orElseThrow(
				() -> new RuntimeException(String.format("Didnt find currency course for %s ", currencyCode)));
	}

	public String getCurrencyWithHighestDeffrenceBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
		uploadDataFromNBPToDB(dateFrom, dateTo);

		List<Object[]> findCountryHavingMoreThanOneCurrency = iExchangeRatesTableRepository
				.findHighestCurrencyCourseDeffrenceBetweenDates();

		Object[] maxDiffrence = findCountryHavingMoreThanOneCurrency.stream()
				.max(Comparator.comparingDouble(d -> ((BigDecimal) d[0]).doubleValue())).orElseThrow(
						() -> new RuntimeException("Didnt find max diffrence between max and min value of currency"));

		return String.format("Highest diffrence of currency course from [%s] to [%s] : was: %s diffrence: [%s]",
				dateFrom.toString(), dateTo.toString(), maxDiffrence[1], maxDiffrence[0]);
	}

	private void uploadDataFromNBPToDB(LocalDate dateFrom, LocalDate dateTo) {
		ExchangeRatesTable[] downloadData = dataProvider
				.downloadData(prepareParamsForNBPAPIDataProvider(dateFrom, dateTo));
		for (ExchangeRatesTable data : downloadData) {
			for (Rates rates : data.getRates()) {
				rates.setExchangeRatesTable(data);
			}
			iExchangeRatesTableRepository.save(data);
		}
	}

	private HashMap<String, Object> prepareParamsForNBPAPIDataProvider(LocalDate dateFrom, LocalDate dateTo) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(ExchangeRatesTableNBPAPIParamsKey.TABLE_TYPE.getParamName(), "A");
		params.put(ExchangeRatesTableNBPAPIParamsKey.DATE_FROM.getParamName(), dateFrom);
		params.put(ExchangeRatesTableNBPAPIParamsKey.DATE_TO.getParamName(), dateTo);
		return params;
	}

	public List<String> getCountryHavingMoreThanOneCurrency() {
		LocalDate now = LocalDate.now();
		uploadDataFromNBPToDB(now.minusDays(1), now.minusDays(1));
		return iRatesRepository.findCountryHavingMoreThanOneCurrency();
	}
}
