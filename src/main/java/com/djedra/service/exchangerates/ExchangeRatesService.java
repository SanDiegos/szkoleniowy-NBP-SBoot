package com.djedra.service.exchangerates;

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
import com.djedra.entity.ICurrecnyDiffResponse;
import com.djedra.entity.Rate;
import com.djedra.exception.ExchangeRatesServiceException;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTablePOJO;
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

	public Rate getHighestExchangeRatesTable(String currencyCode, LocalDate dateFrom, LocalDate dateTo) {
		uploadDataToDB(null, dateFrom, dateTo);
		return rateRepository.findFirstByCurrency_CodeAndDateBetweenOrderByMidDesc(currencyCode, dateFrom, dateTo);
	}

	public String getCurrencyWithHighestDiffrenceBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
		uploadDataToDB(null, dateFrom, dateTo);
		ICurrecnyDiffResponse highestDiffrenceObj = currencyRepository
				.findHighestCurrencyCourseDeffrenceBetweenDatesObj();
		String currencyCode = highestDiffrenceObj.getCurrencyCode();
		Long currencyId = highestDiffrenceObj.getCurrencyId();
		BigDecimal diffrence = highestDiffrenceObj.getDiffrence();

		List<Object[]> highestDiffrence = currencyRepository.findHighestCurrencyCourseDeffrenceBetweenDates();
		if (Objects.isNull(highestDiffrence) || highestDiffrence.size() != 1) {
			throw new ExchangeRatesServiceException("Data not found");

		}
		return String.format("Highest diffrence of currency course from [%s] to [%s] : was: %s diffrence: [%s]",
				dateFrom.toString(), dateTo.toString(), highestDiffrence.get(0)[1], highestDiffrence.get(0)[0]);
	}

	public List<Country> getCountryHavingMoreThanOneCurrency() {
		LocalDate now = LocalDate.now();
		uploadDataToDB(null, now.minusDays(2), now.minusDays(1));
		saveWithoutDuplicates(
				DataFactory.getData(now.minusDays(2), BigDecimal.ONE, "dolar amerykański", "SomeCurrency"));
		List<Country> countries = currencyToCountryRepository.findCountryHavingMoreThanOneCurrency();
		return countries;
	}

	public List<Rate> getFiveHighestOrLowestCurrencyCourse(ExchangeRatesTableTypes tableType, String currencyCode,
			LocalDate dateFrom, LocalDate dateTo, boolean topHigh) {
		uploadDataToDB(tableType, dateFrom, dateTo);
		return topHigh
				? rateRepository.findTop5ByCurrency_CodeAndDateBetweenOrderByMidDesc(currencyCode, dateFrom, dateTo)
				: rateRepository.findTop5ByCurrency_CodeAndDateBetweenOrderByMidAsc(currencyCode, dateFrom, dateTo);
	}

	@Transactional
	private void saveWithoutDuplicates(Currency currency2) {
		Currency currencyFromRepo = currencyRepository.findByCode(currency2.getCode());
		if (Objects.nonNull(currencyFromRepo)) {
			List<CurrencyToCountry> currencyToRate = currencyFromRepo.getCurrencyToCountry();
			List<String> currencyCodes = currencyToRate.stream().map(ctr -> ctr.getCurrency().getCode())
					.collect(Collectors.toList());
			if (!currencyCodes.contains(currency2.getCode())) {
				Country save = countryRepository.save(currency2.getCurrencyToCountry().get(0).getCountry());
				CurrencyToCountry currToCount = new CurrencyToCountry(currency2, save);
				currencyToCountryRepository.save(currToCount);
			}

			List<String> collect = currency2.getCurrencyToCountry().stream().map(ctr -> ctr.getCountry().getName())
					.collect(Collectors.toList());
			List<String> currencyCountries = currencyToRate.stream().map(ctr -> ctr.getCountry().getName())
					.collect(Collectors.toList());
			for (String c : collect) {
				if (!currencyCountries.contains(c)) {
					Country save = countryRepository.save(currency2.getCurrencyToCountry().get(0).getCountry());
					CurrencyToCountry currToCount = new CurrencyToCountry(currencyFromRepo, save);
					currency2.setCurrencyToCountry(Arrays.asList(currToCount));
					currencyToCountryRepository.save(currToCount);
				}
			}
		} else {
			Currency addedCurr = currencyRepository.save(currency2);
			Country countryFromRepo = countryRepository
					.findByname(currency2.getCurrencyToCountry().get(0).getCountry().getName());
			if (Objects.nonNull(countryFromRepo)) {
				CurrencyToCountry currToCount = new CurrencyToCountry(addedCurr, countryFromRepo);
				currencyToCountryRepository.save(currToCount);
			} else {
				if (Objects.nonNull(addedCurr)) {
					countryRepository.save(addedCurr.getCurrencyToCountry().get(0).getCountry());
				}
				currencyToCountryRepository.saveAll(currency2.getCurrencyToCountry());
			}
		}
		addRateIfCurrencyExsists(currency2);
	}

	@Transactional
	private void addRateIfCurrencyExsists(Currency currency2) {
		Currency currencyFromRepo = currencyRepository.findByCode(currency2.getCode());
		if (Objects.nonNull(currencyFromRepo)) {
			List<Rate> curr2Rates = currency2.getRates();
			curr2Rates.forEach(r -> r.setCurrency(currencyFromRepo));
			for (Rate rate : curr2Rates) {
				Currency exsistingCurrency = currencyRepository.findByCodeAndRates_Date(currency2.getCode(),
						rate.getDate());
				if (Objects.isNull(exsistingCurrency)) {
					rateRepository.save(rate);
				}
			}
		}
	}

	@Transactional
	private void uploadDataToDB(ExchangeRatesTableTypes tableType, LocalDate dateFrom, LocalDate dateTo) {
		NBPExchangeRatesTablePOJO[] downloadedData = dataProvider
				.downloadData(prepareParamsForNBPAPIDataProvider(tableType, dateFrom, dateTo));
		if (Objects.nonNull(downloadedData)) {
			List<Currency> dataFromNBP = parser.parse(downloadedData);
			dataFromNBP.forEach(c -> saveWithoutDuplicates(c));
		}
	}

	private HashMap<String, Object> prepareParamsForNBPAPIDataProvider(ExchangeRatesTableTypes tableType,
			LocalDate dateFrom, LocalDate dateTo) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(ExchangeRatesTableNBPAPIParamsKey.TABLE_TYPE.getParamName(),
				Objects.isNull(tableType) ? "A" : tableType.getValue());
		params.put(ExchangeRatesTableNBPAPIParamsKey.DATE_FROM.getParamName(), dateFrom);
		params.put(ExchangeRatesTableNBPAPIParamsKey.DATE_TO.getParamName(), dateTo);
		return params;
	}

}
