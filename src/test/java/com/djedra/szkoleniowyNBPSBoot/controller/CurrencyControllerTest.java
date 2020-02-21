package com.djedra.szkoleniowyNBPSBoot.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import com.djedra.controller.CurrencyController;
import com.djedra.entity.currency.Currency;
import com.djedra.entity.currency.Rate;
import com.djedra.facade.CurrencyFacade;
import com.djedra.repository.currency.ICurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CurrencyController.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CurrencyControllerTest {

	private final Rate validRate = new Rate(2L, null, CurrencyDataFactory.VALID_EFFECTIVE_DATA,
			BigDecimal.valueOf(11.11), "no2", BigDecimal.valueOf(22.22), BigDecimal.valueOf(33.33));

	private final Currency currencyValidData = CurrencyDataFactory
			.createValidData(Arrays.asList(validRate));

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	ICurrencyRepository currencyRepository;
	@MockBean
	private CurrencyFacade currencyFacade;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	private void init() {
		when(currencyFacade.getExchangeRateForDate(currencyValidData.getTableType(),
				currencyValidData.getCode(), CurrencyDataFactory.VALID_EFFECTIVE_DATA))
						.thenReturn(currencyValidData);
	}

	@Test
	public void getExchangeRateForDate() throws Exception {

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
				requestParams.add("tableType", currencyValidData.getTableType());
				requestParams.add("currencyCode", currencyValidData.getCode());
				requestParams.add("date", CurrencyDataFactory.VALID_EFFECTIVE_DATA.toString());
				
		String contentAsString = this.mockMvc.perform(get("/currency/get-by-date").params(requestParams)).andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		Currency currency = objectMapper.readValue(contentAsString, Currency.class);

		assertAll(() -> {
			assertEquals(currencyValidData, currency);
			assertEquals(validRate, currencyValidData.getRates().get(0));
		});

	}
}
