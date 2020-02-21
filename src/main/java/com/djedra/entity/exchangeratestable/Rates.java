package com.djedra.entity.exchangeratestable;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
public class Rates {

	public Rates(Long id, String code, String currency, BigDecimal rate, ExchangeRatesTable exchangeRatesTable) {
		super();
		this.id = id;
		this.code = code;
		this.currency = currency;
		this.rate = rate;
		this.exchangeRatesTable = exchangeRatesTable;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String currency;
	@JsonProperty("mid")
	private BigDecimal rate;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "echangeRatesTable_Id")
	@JsonBackReference
	private ExchangeRatesTable exchangeRatesTable;
	
}
