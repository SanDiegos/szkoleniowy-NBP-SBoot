package com.djedra.entity.currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
//@Table(name = "currency_table")
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonProperty("table")
	private String tableType;
	private String currency;
	private String code;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "currency", fetch = FetchType.EAGER)
	@JsonProperty("rates")
	private List<RateCurrency> rates;

	public BigDecimal getRate() {
		if (Objects.isNull(rates) || rates.isEmpty() || Objects.isNull(rates.get(0).getRate())) {
			return null;
		}
		return rates.get(0).getRate();
	}
}
