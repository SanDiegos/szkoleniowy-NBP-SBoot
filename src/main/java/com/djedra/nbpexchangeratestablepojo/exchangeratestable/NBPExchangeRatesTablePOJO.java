package com.djedra.nbpexchangeratestablepojo.exchangeratestable;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "table", "no", "effectiveDate", "rates" })
public class NBPExchangeRatesTablePOJO {

	@JsonProperty("table")
	private String table;
	@JsonProperty("no")
	private String no;
	@JsonProperty("effectiveDate")
	private LocalDate effectiveDate;
	@JsonProperty("rates")
	private List<NBPExchangeRatesTableRatesPOJO> rates = null;

	@JsonProperty("table")
	public String getTable() {
		return table;
	}

	@JsonProperty("table")
	public void setTable(String table) {
		this.table = table;
	}

	@JsonProperty("no")
	public String getNo() {
		return no;
	}

	@JsonProperty("no")
	public void setNo(String no) {
		this.no = no;
	}

	@JsonProperty("effectiveDate")
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	@JsonProperty("effectiveDate")
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@JsonProperty("rates")
	public List<NBPExchangeRatesTableRatesPOJO> getRates() {
		return rates;
	}

	@JsonProperty("rates")
	public void setRates(List<NBPExchangeRatesTableRatesPOJO> rates) {
		this.rates = rates;
	}

}
