package com.djedra.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Rate {

	public Rate() {
	};

	public Rate(LocalDate date, BigDecimal mid) {
		super();
		this.date = date;
		this.mid = mid;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	private BigDecimal mid;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "Currency_Id")
	@JsonBackReference
	private Currency currency;
}
