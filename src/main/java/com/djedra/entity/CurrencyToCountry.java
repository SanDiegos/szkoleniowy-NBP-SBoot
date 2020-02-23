package com.djedra.entity;

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
public class CurrencyToCountry {

	public CurrencyToCountry() {
		super();
	}

	public CurrencyToCountry(Currency currency, Country country) {
		super();
		this.currency = currency;
		this.country = country;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "currency_Id")
	@JsonBackReference
	private Currency currency;
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "country_Id")
	@JsonBackReference
	private Country country;
}
