package com.djedra.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.Rate;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTablePOJO;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTableRatesPOJO;

@Component
public class NBPExchangeRatesTablePOJOToCurrencyParser implements IParser<NBPExchangeRatesTablePOJO[], List<Currency>> {

	@Override
	public List<Currency> parse(NBPExchangeRatesTablePOJO[] dataToParse) {
		if (Objects.isNull(dataToParse)) {
			return null;
		}
		List<Currency> currencysToRet = new ArrayList<Currency>();
//		List<Rate> ratesToRet = new ArrayList<Rate>();
//		Set<Country> countries = new HashSet<Country>();
		List<Country> countries = new ArrayList<Country>();
		for (NBPExchangeRatesTablePOJO nbptable : dataToParse) {
			List<NBPExchangeRatesTableRatesPOJO> nbprates = nbptable.getRates();
			for (NBPExchangeRatesTableRatesPOJO nbpRate : nbprates) {
				Currency currency = new Currency();
				Rate rates = new Rate(nbptable.getEffectiveDate(), nbpRate.getMid());
				Country country = new Country(nbpRate.getCurrency());
				Optional<Country> findFirstCountry = countries.stream()
						.filter(c -> c.getName().equals(nbpRate.getCurrency())).findFirst();
				if (findFirstCountry.isPresent()) {
					country = findFirstCountry.get();
				} else {
					countries.add(country);
				}
//				List<Country> countries = new ArrayList<Country>();

				Optional<Currency> findFirstCurrency = currencysToRet.stream()
						.filter(c -> c.getCode().equals(nbpRate.getCode())).findFirst();
//				if (findFirstCurrency.isPresent()) {
//					currency = findFirstCurrency.get();
//					currency.setRates(Arrays.asList(rates));
//					currency.setCountry(Arrays.asList(country));
//					currencysToRet.add(currency);
//				} else {
//					currency = new Currency(nbpRate.getCode(), Arrays.asList(rates), Arrays.asList(country));
//					currencysToRet.add(currency);
//				}
			}
		}
		return currencysToRet;
	}

//	List<NBPExchangeRatesTableRatesPOJO> nbprates = data.getRates();
//
//	if (Objects.nonNull(nbprates)) {
//		for (NBPExchangeRatesTableRatesPOJO nbprate : nbprates) {
//			Rate rate = new Rate(data.getEffectiveDate(), null);
//			rate.setMid(nbprate.getMid());
//			countries.add(new Country(nbprate.getCurrency()));
//			Currency currency = new Currency(nbprate.getCode(), rates, countries);
//			// rates.forEach(r -> r.setCurrency(currency));
//			for (Country country : countries) {
//				Set<Currency> curr = new HashSet<Currency>();
//				curr.add(currency);
//				country.setCurrency(curr);
//			}
//			rate.setCurrency(currency);
//			rates.add(rate);
//			currencys.add(currency);
//		}
//	}

}
