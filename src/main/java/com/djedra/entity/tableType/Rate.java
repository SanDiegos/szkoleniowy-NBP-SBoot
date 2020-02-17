package com.djedra.entity.tableType;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Rate {
	private String currency;
	private String code;
	@JsonProperty("mid")
	private BigDecimal rate;
	private BigDecimal bid;
	private BigDecimal ask;
}
