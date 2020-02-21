package com.djedra.service.exchangeratestable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTablePOJO;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTableRatesPOJO;
import com.djedra.parser.IParser;
import com.djedra.repository.ICountryRepository;
import com.djedra.repository.ICurrencyRepository;
import com.djedra.util.Constants.ExchangeRatesTableNBPAPIParamsKey;
import com.djedra.util.Constants.ExchangeRatesTableTypes;

@Service
public class ExchangeRatesService {

	private IDataProvider<NBPExchangeRatesTablePOJO[]> dataProvider;
	private IParser<NBPExchangeRatesTablePOJO[], List<Currency>> parser;
	private ICurrencyRepository currencyRepository;
	private ICountryRepository countryRepository;

	@Autowired
	public ExchangeRatesService(IDataProvider<NBPExchangeRatesTablePOJO[]> dataProvider,
			IParser<NBPExchangeRatesTablePOJO[], List<Currency>> parser, ICurrencyRepository currencyRepository,
			ICountryRepository countryRepository) {
		this.dataProvider = dataProvider;
		this.parser = parser;
		this.currencyRepository = currencyRepository;
		this.countryRepository = countryRepository;
	}

	public BigDecimal getHighestExchangeRatesTable(String currencyCode, LocalDate dateFrom, LocalDate dateTo) {
		uploadDataToDB(null, dateFrom, dateTo);
//		uploadDataToDB(null, dateFrom, dateTo);
//		return findAny.orElseThrow(
//				() -> new ExchangeRatesTableServiceException(String.format("Didnt find currency course for %s ", currencyCode)));
		return BigDecimal.ONE;
	}

	public String getCurrencyWithHighestDeffrenceBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
		uploadDataToDB(null, dateFrom, dateTo);


//		Object[] maxDiffrence = findCountryHavingMoreThanOneCurrency.stream()
//				.max(Comparator.comparingDouble(d -> ((BigDecimal) d[0]).doubleValue())).orElseThrow(
//						() -> new RuntimeException("Didnt find max diffrence between max and min value of currency"));

		return "";
	}

	@Transactional
	private void uploadDataToDB(ExchangeRatesTableTypes tableType, LocalDate dateFrom, LocalDate dateTo) {
		NBPExchangeRatesTablePOJO[] downloadedData = dataProvider
				.downloadData(prepareParamsForNBPAPIDataProvider(tableType, dateFrom, dateTo));
		if (Objects.nonNull(downloadedData)) {
			List<Currency> dataFromNBP = new ArrayList<Currency>();
			currencyRepository.saveAll(parser.parse(downloadedData));
			Optional<Set<Country>> countries = dataFromNBP.parallelStream().map(d -> d.getCountry()).findAny();
			if (countries.isPresent()) {
				countryRepository.saveAll(countries.get());
			}
		}
	}

//	@Transactional
//	private void uploadDataFromNBPToDB(ExchangeRatesTableTypes tableType, LocalDate dateFrom, LocalDate dateTo) {
//		NBPExchangeRatesTablePOJO[] downloadedData = dataProvider
//				.downloadData(prepareParamsForNBPAPIDataProvider(tableType, dateFrom, dateTo));
//		if (Objects.nonNull(downloadedData)) {
//			List<Currency> dataFromNBP = new ArrayList<Currency>();
//			currencyRepository.saveAll(parser.parse(downloadedData));
//			Optional<Set<Country>> countries = dataFromNBP.parallelStream().map(d -> d.getCountry()).findAny();
//			if (countries.isPresent()) {
//				countryRepository.saveAll(countries.get());
//			}
//		}
//	}

//	ExchangeRatesTable[] downloadedData = dataProvider
//			.downloadData(prepareParamsForNBPAPIDataProvider(null, dateFrom, dateTo));
//	if (Objects.nonNull(downloadedData)) {
//		for (ExchangeRatesTable data : downloadedData) {
//			for (Rates rates : data.getRates()) {
//				rates.setExchangeRatesTable(data);
//			}
//			iExchangeRatesTableRepository.save(data);
//		}
//	}

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
		uploadDataToDB(null, now.minusDays(2), now.minusDays(1));
//		List<Rates> rates = iRatesRepository.findCountryHavingMoreThanOneCurrency();
		return Arrays.asList("");
	}

	public List<NBPExchangeRatesTableRatesPOJO> getFiveHighestOrLowestCurrencyCourse(ExchangeRatesTableTypes tableType, String currencyCode,
			LocalDate dateFrom, LocalDate dateTo, boolean topHigh) {
		uploadDataToDB(tableType, dateFrom, dateTo);
//		List<ExchangeRatesTable> top5 = iExchangeRatesTableRepository
//				.findTop5ByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo);
//
//		List<ExchangeRatesTable> low5 = iExchangeRatesTableRepository
//				.findTop5ByRates_CodeAndEffectiveDateBetweenOrderByRates_rateAsc(currencyCode, dateFrom, dateTo);

		return null;
	}
}
