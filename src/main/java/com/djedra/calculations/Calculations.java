package com.djedra.calculations;

import java.math.BigDecimal;

public class Calculations {

	public static BigDecimal exchange(BigDecimal amount, BigDecimal rate) {
		return amount.multiply(rate);
	}
}
