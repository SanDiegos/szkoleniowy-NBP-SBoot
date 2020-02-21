package com.djedra.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.djedra.entity.Country;
import com.djedra.entity.Currency;
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
		Set<Country> countries = new HashSet<Country>();
		if (Objects.nonNull(nbprates) && nbprates.size() == 1) {
			NBPCurrencyRatePOJO nbpCurrencyRatePOJO = nbprates.get(0);
			rates.add(new Rate(nbpCurrencyRatePOJO.getEffectiveDate(), nbpCurrencyRatePOJO.getMid()));
		}
		countries.add(new Country(data.getCurrency()));
		Currency currency = new Currency(data.getCode(), rates, countries);
		rates.forEach(r -> r.setCurrency(currency));
		for (Country country : countries) {
			Set<Currency> curr = new HashSet<Currency>();
			curr.add(currency);
			country.setCurrency(curr);
		}
		return currency;
	}

}
