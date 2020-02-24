package com.djedra.entity;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;

public interface ICurrecnyDiffResponse {

	@Value("#{target.Currency_id}")
	Long getCurrencyId();

	@Value("#{target.code}")
	String getCurrencyCode();

	@Value("#{target.diff}")
	BigDecimal getDiffrence();
}
