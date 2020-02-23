package com.djedra.nbpjsontopojo.currency;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "table", "currency", "code", "rates" })
public class NBPCurrencyPOJO {

	@JsonProperty("table")
	private String table;
	@JsonProperty("currency")
	private String currency;
	@JsonProperty("code")
	private String code;
	@JsonProperty("rates")
	private List<NBPCurrencyRatePOJO> rates = null;

	@JsonProperty("table")
	public String getTable() {
		return table;
	}

	@JsonProperty("table")
	public void setTable(String table) {
		this.table = table;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("currency")
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("rates")
	public List<NBPCurrencyRatePOJO> getRates() {
		return rates;
	}

	@JsonProperty("rates")
	public void setRates(List<NBPCurrencyRatePOJO> rates) {
		this.rates = rates;
	}


}
