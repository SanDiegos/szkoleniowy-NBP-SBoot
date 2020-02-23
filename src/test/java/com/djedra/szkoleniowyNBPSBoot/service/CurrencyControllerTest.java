package com.djedra.szkoleniowyNBPSBoot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.NestedServletException;

import com.djedra.controller.CurrencyController;
import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.CurrencyToCountry;
import com.djedra.entity.Rate;
import com.djedra.facade.CurrencyFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CurrencyController.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CurrencyControllerTest {

	private String currencyCode = "curr";
	private LocalDate date = LocalDate.of(2020, 02, 20);

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private CurrencyFacade currencyFacade;

	private BigDecimal mid = BigDecimal.valueOf(11);
	private Rate rate = new Rate(date, mid);
	private Country country = new Country("country");
	private CurrencyToCountry currencyToCountry = new CurrencyToCountry(null, country);
	private Currency currency = new Currency(1L, currencyCode, Arrays.asList(rate), Arrays.asList(currencyToCountry));

	@BeforeAll
	private void init() {
		when(currencyFacade.getExchangeRateForDate("A", currencyCode, date)).thenReturn(currency);
	}

	@Test
	public void get_exchange_rate_for_date_return_valid_data() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("tableType", Arrays.asList("A"));
		params.put("currencyCode", Arrays.asList(currencyCode));
		params.put("date", Arrays.asList(date.toString()));

		String contentAsString = this.mockMvc.perform(get("/currency/get-by-date").params(params))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Currency returnedCurrency = objectMapper.readValue(contentAsString, Currency.class);
		assertEquals(currency, returnedCurrency);
	}

	@Test
	public void get_exchange_rate_for_date_throw_validation_exception() throws Exception {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("tableType", Arrays.asList("A"));
		params.put("currencyCode", Arrays.asList(currencyCode));
		params.put("date", Arrays.asList(LocalDate.now().plusDays(1).toString()));

		Exception exception = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/currency/get-by-date").params(params));
		});

		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains("You cannot search for course currency in future"));

	}

}
