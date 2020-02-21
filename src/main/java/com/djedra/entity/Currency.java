package com.djedra.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Currency {
	
	public Currency() {
	};

	public Currency(String code, List<Rate> rates, Set<Country> country) {
		super();
		this.code = code;
		this.rates = rates;
		this.country = country;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "currency")
	@JsonManagedReference
	private List<Rate> rates;
	@ManyToMany(mappedBy = "currency")
	@JsonManagedReference
	private Set<Country> country;

	public BigDecimal getRate() {
		if (Objects.isNull(rates) || rates.isEmpty() || Objects.isNull(rates.get(0).getMid())) {
			return null;
		}
		return rates.get(0).getMid();
	}

}
