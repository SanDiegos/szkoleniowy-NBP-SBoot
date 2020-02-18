package com.djedra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.currency.Rate;

public interface IRateRepository extends JpaRepository<Rate, Long> {

}
