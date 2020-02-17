package com.djedra.entity.currency;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Rate {

	private String effectiveDate;
	@JsonProperty("mid")
	private BigDecimal rate;
	@JsonProperty("no")
	private String id;
	private BigDecimal bid;
	private BigDecimal ask;
}
