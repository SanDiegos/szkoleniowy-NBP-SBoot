package com.djedra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.Country;

public interface ICountryRepository extends JpaRepository<Country, Long> {

	public Country findByname(String name);

}
