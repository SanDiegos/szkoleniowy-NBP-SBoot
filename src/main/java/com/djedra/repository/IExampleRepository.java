package com.djedra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.tableType.Example;

public interface IExampleRepository extends JpaRepository<Example, Long> {

	public Example findBytableType(String table);
}
