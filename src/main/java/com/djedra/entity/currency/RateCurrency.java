package com.djedra.entity.currency;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
//@Entity
public class RateCurrency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "currency_id", nullable = false)
	private Currency currency;
	
	private String effectiveDate;
	@JsonProperty("mid")
	private BigDecimal rate;
	private String no;
	private BigDecimal bid;
	private BigDecimal ask;
}
