package com.djedra.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//@Table(name="Currency", 
//uniqueConstraints=
//    @UniqueConstraint(columnNames={"code"})
public class Currency {

	public Currency() {
	};

	public Currency(String code) {
		super();
		this.code = code;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String code;
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "currency")
	@JsonManagedReference
	private List<Rate> rates;
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "currency")
	@JsonManagedReference
	private List<CurrencyToCountry> currencyToRates;

	public BigDecimal getRate() {
		if (Objects.isNull(rates) || rates.isEmpty() || Objects.isNull(rates.get(0).getMid())) {
			return null;
		}
		return rates.get(0).getMid();
	}

}
