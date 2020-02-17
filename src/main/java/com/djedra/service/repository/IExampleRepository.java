package com.djedra.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.tableType.Example;

public interface IExampleRepository extends JpaRepository<Example, Integer>{

	public Example findByTable(String table);
}
