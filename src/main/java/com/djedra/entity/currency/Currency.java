package com.djedra.entity.currency;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.djedra.entity.IEntityHead;

import lombok.Data;

@Data
@Entity
//@Table(name = "currency_table")
public class Currency{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String table;
	@Column
	private String currency;
	@Column
	private String code;
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "currency", fetch = FetchType.EAGER)
//	private List<RateCurrency> rates;

//	public BigDecimal getRate() {
//		if (Objects.isNull(rates) || rates.isEmpty() || Objects.isNull(rates.get(0).getRate())) {
//			return null;
//		}
//		return rates.get(0).getRate();
//	}
	public BigDecimal getRate() {
		return BigDecimal.ZERO;
	}
}
