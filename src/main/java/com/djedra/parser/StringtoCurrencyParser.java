package com.djedra.parser;

import java.io.IOException;

import com.djedra.entity.currency.Currency;
import com.djedra.exception.ParserException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringtoCurrencyParser implements IParser<String, Currency> {

	@Override
	public Currency parse(String data) {
		Currency parsed = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			parsed = mapper.readValue(data, Currency.class);
		} catch (IOException e) {
			throw new ParserException("Error while parsing downloaded format to POJO", e);
		}
		return parsed;
	}


}
