package com.djedra.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.CurrencyToCountry;
import com.djedra.entity.Rate;
import com.djedra.nbpjsontopojo.currency.NBPCurrencyPOJO;
import com.djedra.nbpjsontopojo.currency.NBPCurrencyRatePOJO;

@Component
public class NBPCurrencyToCurrencyParser implements IParser<NBPCurrencyPOJO, Currency> {

	@Override
	public Currency parse(NBPCurrencyPOJO data) {
		if (Objects.isNull(data)) {
			return null;
		}
		List<NBPCurrencyRatePOJO> nbprates = data.getRates();
		List<Rate> rates = new ArrayList<Rate>();
		List<Country> countries = new ArrayList<Country>();
		if (Objects.nonNull(nbprates) && nbprates.size() == 1) {
			NBPCurrencyRatePOJO nbpCurrencyRatePOJO = nbprates.get(0);
			rates.add(new Rate(nbpCurrencyRatePOJO.getEffectiveDate(), nbpCurrencyRatePOJO.getMid()));
		}
		countries.add(new Country(data.getCurrency()));
		Currency currency = new Currency(data.getCode());
		rates.forEach(r -> r.setCurrency(currency));
		CurrencyToCountry currToCountry = new CurrencyToCountry();
		for (Country country : countries) {
			currToCountry.setCountry(country);
			currToCountry.setCurrency(currency);
		}
		currency.setRates(rates);
		currency.setCurrencyToCountry(Arrays.asList(currToCountry));
		return currency;
	}

}
