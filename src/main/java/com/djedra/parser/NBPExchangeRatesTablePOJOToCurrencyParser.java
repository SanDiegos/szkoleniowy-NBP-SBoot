package com.djedra.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.djedra.entity.Currency;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTablePOJO;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTableRatesPOJO;
import com.djedra.util.DataFactory;

@Component
public class NBPExchangeRatesTablePOJOToCurrencyParser implements IParser<NBPExchangeRatesTablePOJO[], List<Currency>> {

	@Override
	public List<Currency> parse(NBPExchangeRatesTablePOJO[] dataToParse) {
		if (Objects.isNull(dataToParse)) {
			return null;
		}
		List<Currency> currencyToRet = new ArrayList<>();

		for (NBPExchangeRatesTablePOJO data : dataToParse) {
			List<NBPExchangeRatesTableRatesPOJO> rates = data.getRates();
			for (NBPExchangeRatesTableRatesPOJO rate : rates) {
				Currency currency = DataFactory.getData(data.getEffectiveDate(), rate.getMid(), rate.getCurrency(),
						rate.getCode());
				currencyToRet.add(currency);
			}
		}
		return currencyToRet;
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
