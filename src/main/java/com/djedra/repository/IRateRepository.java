package com.djedra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.Rate;

public interface IRateRepository extends JpaRepository<Rate, Long> {

//	public Rate findByCurrency_CodeAndRate_Date(String currencyCode, LocalDate date);

}
