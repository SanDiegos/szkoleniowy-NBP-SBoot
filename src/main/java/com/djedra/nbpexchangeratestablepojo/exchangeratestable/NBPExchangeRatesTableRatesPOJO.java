package com.djedra.nbpexchangeratestablepojo.exchangeratestable;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "currency", "code", "mid" })
public class NBPExchangeRatesTableRatesPOJO {

	@JsonProperty("currency")
	private String currency;
	@JsonProperty("code")
	private String code;
	@JsonProperty("mid")
	private BigDecimal mid;

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

	@JsonProperty("mid")
	public BigDecimal getMid() {
		return mid;
	}

	@JsonProperty("mid")
	public void setMid(BigDecimal mid) {
		this.mid = mid;
	}

}

