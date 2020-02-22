package com.djedra.service.exchangeratestable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.djedra.connection.IDataProvider;
import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.CurrencyToCountry;
import com.djedra.entity.Rate;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTablePOJO;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTableRatesPOJO;
import com.djedra.parser.IParser;
import com.djedra.repository.ICountryRepository;
import com.djedra.repository.ICurrencyRepository;
import com.djedra.repository.ICurrencyToCountryRepository;
import com.djedra.repository.IRateRepository;
import com.djedra.util.Constants.ExchangeRatesTableNBPAPIParamsKey;
import com.djedra.util.Constants.ExchangeRatesTableTypes;
import com.djedra.util.DataFactory;

@Service
public class ExchangeRatesService {

	private IDataProvider<NBPExchangeRatesTablePOJO[]> dataProvider;
	private IParser<NBPExchangeRatesTablePOJO[], List<Currency>> parser;
	private ICurrencyRepository currencyRepository;
	private ICountryRepository countryRepository;
	private IRateRepository rateRepository;
	private ICurrencyToCountryRepository currencyToCountryRepository;

	@Autowired
	public ExchangeRatesService(IDataProvider<NBPExchangeRatesTablePOJO[]> dataProvider,
			IParser<NBPExchangeRatesTablePOJO[], List<Currency>> parser, ICurrencyRepository currencyRepository,
			ICountryRepository countryRepository, ICurrencyToCountryRepository currencyToRateRepository,
			IRateRepository rateRepository) {
		this.dataProvider = dataProvider;
		this.parser = parser;
		this.currencyRepository = currencyRepository;
		this.countryRepository = countryRepository;
		this.rateRepository = rateRepository;
		this.currencyToCountryRepository = currencyToRateRepository;
	}

	public BigDecimal getHighestExchangeRatesTable(String currencyCode, LocalDate dateFrom, LocalDate dateTo) {
//		uploadDataToDB(null, dateFrom, dateTo);

//		uploadDataToDB(null, dateFrom, dateTo);
//		return findAny.orElseThrow(
//				() -> new ExchangeRatesTableServiceException(String.format("Didnt find currency course for %s ", currencyCode)));
		return BigDecimal.ONE;
	}

	public String getCurrencyWithHighestDeffrenceBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
//		uploadDataToDB(null, dateFrom, dateTo);
		Currency currency = DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(1), "Polska", "Zloty");
		Currency currency2 = DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(2), "Polska", "Zloty");
		Currency currency3 = DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(3), "Niemcy", "Zloty");
		Currency currency4 = DataFactory.getData(LocalDate.now(), BigDecimal.valueOf(44), "Niemcy", "Euro");

		currency = currencyRepository.save(currency);
		countryRepository.save(currency.getCurrencyToRates().get(0).getCountry());
		rateRepository.saveAll(currency.getRates());
		currencyToCountryRepository.saveAll(currency.getCurrencyToRates());

		save(currency2);
		save(currency3);
		save(currency4);

//		}

//		currencyRepository.save(currency2);
//		countryRepository.saveAll(currency2.getCountry());
//		Object[] maxDiffrence = findCountryHavingMoreThanOneCurrency.stream()
//				.max(Comparator.comparingDouble(d -> ((BigDecimal) d[0]).doubleValue())).orElseThrow(
//						() -> new RuntimeException("Didnt find max diffrence between max and min value of currency"));

		return "";

	}

	private void save(Currency currency2) {
		Currency currencyFromRepo = currencyRepository.findByCode(currency2.getCode());
		if (Objects.nonNull(currencyFromRepo)) {
			List<CurrencyToCountry> currencyToRate = currencyFromRepo.getCurrencyToRates();
			List<String> currencyCodes = currencyToRate.stream().map(ctr -> ctr.getCurrency().getCode())
					.collect(Collectors.toList());
			List<String> currencyCountrys = currencyToRate.stream().map(ctr -> ctr.getCountry().getName())
					.collect(Collectors.toList());
			if (!currencyCodes.contains(currency2.getCode())) {
				Country save = countryRepository.save(currency2.getCurrencyToRates().get(0).getCountry());
				CurrencyToCountry currToCount = new CurrencyToCountry(currency2, save);
				currencyToCountryRepository.save(currToCount);
			}
			List<String> collect = currency2.getCurrencyToRates().stream().map(ctr -> ctr.getCountry().getName())
					.collect(Collectors.toList());
			for (String c : collect) {
				if (!currencyCountrys.contains(c)) {
					Country save = countryRepository.save(currency2.getCurrencyToRates().get(0).getCountry());
					CurrencyToCountry currToCount = new CurrencyToCountry(currencyFromRepo, save);
					currency2.setCurrencyToRates(Arrays.asList(currToCount));
					currencyToCountryRepository.save(currToCount);
				}
			}
		} else {
			Currency addedCurr = currencyRepository.save(currency2);
			Country countryFromRepo = countryRepository
					.findByname(currency2.getCurrencyToRates().get(0).getCountry().getName());
			if (Objects.nonNull(countryFromRepo)) {
				CurrencyToCountry currToCount = new CurrencyToCountry(addedCurr, countryFromRepo);
				currencyToCountryRepository.save(currToCount);
			} else {
				countryRepository.save(addedCurr.getCurrencyToRates().get(0).getCountry());
			}
		}
		addRateIfCurrencyExsists(currency2);
	}

	private void addRateIfCurrencyExsists(Currency currency2) {
		Currency currencyFromRepo = currencyRepository.findByCode(currency2.getCode());
		if (Objects.nonNull(currencyFromRepo)) {
			List<Rate> curr2Rates = currency2.getRates();
			curr2Rates.forEach(r -> r.setCurrency(currencyFromRepo));
			rateRepository.saveAll(curr2Rates);
		}
	}

	@Transactional
	private void uploadDataToDB(ExchangeRatesTableTypes tableType, LocalDate dateFrom, LocalDate dateTo) {
		NBPExchangeRatesTablePOJO[] downloadedData = dataProvider
				.downloadData(prepareParamsForNBPAPIDataProvider(tableType, dateFrom, dateTo));
		if (Objects.nonNull(downloadedData)) {
			List<Currency> dataFromNBP = parser.parse(downloadedData);
//			for (Currency currency : dataFromNBP) {
//				Currency currencyFromRepo = currencyRepository.findByCode(currency.getCode());
//				if (Objects.nonNull(currencyFromRepo)) {
//					currency = currencyFromRepo;
//				}
//				Country country = currency.getCountry().iterator().next();
//				Country countryFromRepo = countryRepository.findByname(country.getName());
//				if (Objects.nonNull(countryFromRepo)) {
//					country = countryFromRepo;
//					currency.setCountry(Arrays.asList(countryFromRepo));
//				}
////				currencyRepository.saveAll(dataFromNBP);
//				currencyRepository.save(currency);
//			}
//			currencyRepository.saveAll(parser.parse(downloadedData));
//			Optional<List<Country>> countries = dataFromNBP.parallelStream().map(d -> d.getCountry()).findAny();
//			countries.ifPresent(c -> countryRepository.saveAll(c));
//			if (countries.isPresent()) {
//				countryRepository.saveAll(countries.get());
//			}
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

	private HashMap<String, Object> prepareParamsForNBPAPIDataProvider(ExchangeRatesTableTypes tableType,
			LocalDate dateFrom, LocalDate dateTo) {
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

	public List<NBPExchangeRatesTableRatesPOJO> getFiveHighestOrLowestCurrencyCourse(ExchangeRatesTableTypes tableType,
			String currencyCode, LocalDate dateFrom, LocalDate dateTo, boolean topHigh) {
		uploadDataToDB(tableType, dateFrom, dateTo);
//		List<ExchangeRatesTable> top5 = iExchangeRatesTableRepository
//				.findTop5ByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo);
//
//		List<ExchangeRatesTable> low5 = iExchangeRatesTableRepository
//				.findTop5ByRates_CodeAndEffectiveDateBetweenOrderByRates_rateAsc(currencyCode, dateFrom, dateTo);

		return null;
	}
}
