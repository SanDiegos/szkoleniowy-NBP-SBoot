package com.djedra.entity.currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.djedra.entity.IEntityHead;

import lombok.Data;

@Data
public class Currency implements IEntityHead {

	private String table;
	private String currency;
	private String code;
	private List<Rate> rates;

	public BigDecimal getRate() {
		if (Objects.isNull(rates) || rates.isEmpty() || Objects.isNull(rates.get(0).getRate())) {
			return null;
		}
		return rates.get(0).getRate();
	}
}
