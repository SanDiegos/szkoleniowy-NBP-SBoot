package com.djedra.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.djedra.entity.tableType.Example;
import com.djedra.service.ExampleService;
import com.djedra.util.EnumUtil;
import com.djedra.util.Constants.ExchangeRatesTableTypes;

@Component
public class ExampleFacade {

	@Autowired
	private ExampleService exampleService;
	
	public Example getExchangeRates(String tableType) {

		ExchangeRatesTableTypes tabType = EnumUtil.getEnumByValue("tableType", tableType,
				ExchangeRatesTableTypes.class);
		return exampleService.getCurrentExchangeRates(tabType);
	}
	
}
