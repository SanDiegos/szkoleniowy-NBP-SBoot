package com.djedra.service.exchangeratestable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.exchangeratestable.ExchangeRatesTable;
import com.djedra.entity.exchangeratestable.Rates;
import com.djedra.exception.ExchangeRatesTableServiceException;
import com.djedra.repository.exchangeratestable.IExchangeRatesTableRepository;
import com.djedra.repository.exchangeratestable.IRatesRepository;
import com.djedra.util.Constants.ExchangeRatesTableNBPAPIParamsKey;
import com.djedra.util.Constants.ExchangeRatesTableTypes;

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
		uploadDataFromNBPToDB(null, dateFrom, dateTo);
		ExchangeRatesTable ratesTable = iExchangeRatesTableRepository
				.findFirstByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo);
		Optional<BigDecimal> findAny = ratesTable.getRates().stream().filter(r -> r.getCode().equals(currencyCode))
				.map(r -> r.getRate()).findAny();
		return findAny.orElseThrow(
				() -> new ExchangeRatesTableServiceException(String.format("Didnt find currency course for %s ", currencyCode)));
	}

	public String getCurrencyWithHighestDeffrenceBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
		uploadDataFromNBPToDB(null, dateFrom, dateTo);

		List<Object[]> highestDiffrence = iExchangeRatesTableRepository
				.findHighestCurrencyCourseDeffrenceBetweenDates();

//		Object[] maxDiffrence = findCountryHavingMoreThanOneCurrency.stream()
//				.max(Comparator.comparingDouble(d -> ((BigDecimal) d[0]).doubleValue())).orElseThrow(
//						() -> new RuntimeException("Didnt find max diffrence between max and min value of currency"));

		return String.format("Highest diffrence of currency course from [%s] to [%s] : was: %s diffrence: [%s]",
				dateFrom.toString(), dateTo.toString(), highestDiffrence.get(0)[1], highestDiffrence.get(0)[0]);
	}

	@Transactional
	private void uploadDataFromNBPToDB(ExchangeRatesTableTypes tableType, LocalDate dateFrom, LocalDate dateTo) {
		ExchangeRatesTable[] downloadedData = dataProvider
				.downloadData(prepareParamsForNBPAPIDataProvider(null, dateFrom, dateTo));
		if (Objects.nonNull(downloadedData)) {
			for (ExchangeRatesTable data : downloadedData) {
				for (Rates rates : data.getRates()) {
					rates.setExchangeRatesTable(data);
				}
				iExchangeRatesTableRepository.save(data);
			}
		}
	}

	private HashMap<String, Object> prepareParamsForNBPAPIDataProvider(ExchangeRatesTableTypes tableType, LocalDate dateFrom, LocalDate dateTo) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(ExchangeRatesTableNBPAPIParamsKey.TABLE_TYPE.getParamName(),
				Objects.isNull(tableType) ? "A" : tableType.getValue());
		params.put(ExchangeRatesTableNBPAPIParamsKey.DATE_FROM.getParamName(), dateFrom);
		params.put(ExchangeRatesTableNBPAPIParamsKey.DATE_TO.getParamName(), dateTo);
		return params;
	}

	public List<String> getCountryHavingMoreThanOneCurrency() {
		LocalDate now = LocalDate.now();
		uploadDataFromNBPToDB(null, now.minusDays(2), now.minusDays(1));
//		List<Rates> rates = iRatesRepository.findCountryHavingMoreThanOneCurrency();
		return iRatesRepository.findCountryHavingMoreThanOneCurrency();
	}

	public List<Rates> getFiveHighestOrLowestCurrencyCourse(ExchangeRatesTableTypes tableType, String currencyCode,
			LocalDate dateFrom, LocalDate dateTo, boolean topHigh) {
		uploadDataFromNBPToDB(tableType, dateFrom, dateTo);
//		List<ExchangeRatesTable> top5 = iExchangeRatesTableRepository
//				.findTop5ByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo);
//
//		List<ExchangeRatesTable> low5 = iExchangeRatesTableRepository
//				.findTop5ByRates_CodeAndEffectiveDateBetweenOrderByRates_rateAsc(currencyCode, dateFrom, dateTo);

		return topHigh ? iRatesRepository.findTop5ByCodeOrderByRateDesc(currencyCode)
				: iRatesRepository.findTop5ByCodeOrderByRateAsc(currencyCode);
	}
}
