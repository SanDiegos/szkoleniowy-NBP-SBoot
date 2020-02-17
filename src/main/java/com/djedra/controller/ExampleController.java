package com.djedra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djedra.entity.tableType.Example;
import com.djedra.facade.ExampleFacade;

@RestController
@RequestMapping(value = "/example")
public class ExampleController {
	
	@Autowired
	private ExampleFacade exampleFacade;
	
	@GetMapping("/get-currency-course-table")
	public Example getExchangeRatesTable(@RequestParam String tableType) {
		return exampleFacade.getExchangeRates(tableType);
	}
}
