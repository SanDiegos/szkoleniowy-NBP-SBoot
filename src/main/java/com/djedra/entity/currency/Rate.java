package com.djedra.entity.currency;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Rate {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "Currency_Id", nullable = false)
	@JsonBackReference
	private Currency currency;
	private LocalDate effectiveDate;
	@JsonProperty("mid")
	private BigDecimal rate;
	private String no;
	private BigDecimal bid;
	private BigDecimal ask;
}
