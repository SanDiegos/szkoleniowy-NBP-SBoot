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

import lombok.Data;

@Data
@Entity
public class Rates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String currency;
	private BigDecimal rate;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "echangeRatesTable_Id", nullable = false)
	@JsonBackReference
	private ExchangeRatesTable exchangeRatesTable;
	
}
