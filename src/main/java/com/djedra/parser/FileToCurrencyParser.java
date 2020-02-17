package com.djedra.parser;

import java.io.File;
import java.io.IOException;

import com.djedra.entity.currency.Currency;
import com.djedra.exception.ParserException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileToCurrencyParser implements IParser<File, Currency> {

	@Override
	public Currency parse(File data) {
		ObjectMapper objMapper = new ObjectMapper();
		Currency readValue = null;
		try {
			readValue = objMapper.readValue(data, Currency.class);
		} catch (JsonParseException e) {
			throw new ParserException("Error while parsing JSON to POJO. Probably JSON have wrong format.", e);
		} catch (JsonMappingException e) {
			throw new ParserException(
					"Error while parsing JSON to POJO. Probably JSON format doesn't match for expected JAVA class.", e);
		} catch (IOException e) {
			throw new ParserException(String.format("Error while reading the file on directory: [%s]", data.getPath()),
					e);
		}
		return readValue;
	}

}
