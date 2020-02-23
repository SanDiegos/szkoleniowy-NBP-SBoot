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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonIgnoreProperties("currencyToCountry")
public class Currency {

	public Currency() {
	};

	public Currency(String code) {
		this.code = code;
	}

	public Currency(Long id, String code, List<Rate> rates, List<CurrencyToCountry> currencyToCountry) {
		super();
		this.id = id;
		this.code = code;
		this.rates = rates;
		this.currencyToCountry = currencyToCountry;
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
	private List<CurrencyToCountry> currencyToCountry;

	public BigDecimal getRate() {
		if (Objects.isNull(rates) || rates.isEmpty() || Objects.isNull(rates.get(0).getMid())) {
			return null;
		}
		return rates.get(0).getMid();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
