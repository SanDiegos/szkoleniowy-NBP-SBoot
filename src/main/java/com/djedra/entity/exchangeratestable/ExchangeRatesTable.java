package com.djedra.entity.exchangeratestable;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
public class ExchangeRatesTable {

	public ExchangeRatesTable(Long id, String tableType, LocalDate effectiveDate, String no, List<Rates> rates) {
		super();
		this.id = id;
		this.tableType = tableType;
		this.effectiveDate = effectiveDate;
		this.no = no;
		this.rates = rates;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonProperty("table")
	private String tableType;
	private LocalDate effectiveDate;
	private String no;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "exchangeRatesTable")
	private List<Rates> rates;
}
