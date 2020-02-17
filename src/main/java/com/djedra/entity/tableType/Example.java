package com.djedra.entity.tableType;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
//@Table(name = "example_table")
public class Example {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonProperty("table")
	private String tableType;
	private String no;
	private String effectiveDate;
	private String tradingDate;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "example", fetch = FetchType.EAGER)
	@JsonProperty("rates")
	private List<RateExample> rates = null;
}
