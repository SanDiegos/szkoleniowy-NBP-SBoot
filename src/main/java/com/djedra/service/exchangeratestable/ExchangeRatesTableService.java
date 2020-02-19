package com.djedra.service.exchangeratestable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
	public ExchangeRatesTableService(IExchangeRatesTableRepository iExchangeRatesTableRepository, IRatesRepository iRatesRepository, IDataProvider<ExchangeRatesTable[]> dataProvider) {
		this.iExchangeRatesTableRepository = iExchangeRatesTableRepository;
		this.dataProvider = dataProvider;
		this.iRatesRepository = iRatesRepository;
	}
	
	public BigDecimal getHighestExchangeRatesTable(String currencyCode, LocalDate dateFrom, LocalDate dateTo) {
		uploadDataFromNBPToDB(dateFrom, dateTo);
		ExchangeRatesTable ratesTable = iExchangeRatesTableRepository.findFirstByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo);
		Optional<BigDecimal> findAny = ratesTable.getRates().stream().filter(r -> r.getCode().equals(currencyCode)).map( r -> r.getRate()).findAny(); 
		return findAny.orElseThrow(() -> new RuntimeException(String.format("Didnt find currency course for %s ", currencyCode)));
	}
	
	public String getCurrencyWithHighestDeffrenceBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
		uploadDataFromNBPToDB(dateFrom, dateTo);
		
		HashMap<String, List<BigDecimal>> mapWithCurrRates = new HashMap<String, List<BigDecimal>>();
		
		List<ExchangeRatesTable> ratesTable = iExchangeRatesTableRepository.findAllByeffectiveDateBetween(dateFrom, dateTo);
		for(ExchangeRatesTable exchangeRatesTable : ratesTable) {
			List<Rates> ratesTab = exchangeRatesTable.getRates();
			for(Rates rates: ratesTab) {
				List<BigDecimal> currRates = mapWithCurrRates.get(rates.getCode());
				if(Objects.isNull(currRates)) {
					currRates = new ArrayList<BigDecimal>();
				}
				if(Objects.nonNull(rates.getRate())) {
					currRates.add(rates.getRate());
				}
				if(!mapWithCurrRates.containsKey(rates.getCode())){
					mapWithCurrRates.put(rates.getCode(), currRates);
				}
			}
		}
		
		HashMap<String, BigDecimal> diffrences = new HashMap<String, BigDecimal>();
		for(String key : mapWithCurrRates.keySet()) {
			List<BigDecimal> currRates = mapWithCurrRates.get(key);
			Optional<BigDecimal> min = currRates.stream().min(Comparator.naturalOrder());
			Optional<BigDecimal> max = currRates.stream().max(Comparator.naturalOrder());
			
			if (min.isPresent() && max.isPresent()) {
				diffrences.put(key, max.get().subtract(min.get()));
			}
		}
		
		String maxDiffKey = null;
		BigDecimal maxDiff = BigDecimal.ZERO;
		for(String key : diffrences.keySet()) {
			BigDecimal diff = diffrences.get(key) ;
			if(diff.compareTo(maxDiff) > 0) {
				maxDiff = diff;
				maxDiffKey = key;
			}
		}
		return String.format("Highest diffrence of currency course from [%s] to [%s] : was: %s diffrence: [%s]", dateFrom.toString(), dateTo.toString(), maxDiffKey, maxDiff);
	}

	private void uploadDataFromNBPToDB(LocalDate dateFrom, LocalDate dateTo) {
		ExchangeRatesTable[] downloadData = dataProvider.downloadData(prepareParamsForNBPAPIDataProvider(dateFrom, dateTo));
		for(ExchangeRatesTable data : downloadData) {
				for (Rates rates: data.getRates()) {
					rates.setExchangeRatesTable(data);
				}
				iExchangeRatesTableRepository.save(data);
		}
	}
	
	private HashMap<String, Object> prepareParamsForNBPAPIDataProvider(LocalDate dateFrom, LocalDate dateTo){
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
