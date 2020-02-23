package com.djedra.szkoleniowyNBPSBoot.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.djedra.connection.IDataProvider;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTablePOJO;
import com.djedra.service.exchangeratestable.ExchangeRatesService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ExchangeRatesServiceTest {

	private BigDecimal highestExchangeRate = BigDecimal.valueOf(3);
	private String currencyCode = "currCode";
	private String currency = "currency";
	private LocalDate dateFrom = LocalDate.of(2020, 02, 12);
	private LocalDate dateTo = LocalDate.of(2020, 02, 15);
//	private Rates rates = new Rates(1L, currencyCode, currency, highestExchangeRate, null);
//	private ExchangeRatesTable highestValueInDates = new ExchangeRatesTable(1L, "A", dateFrom, "no",
//			Arrays.asList(rates));

	@MockBean
	private IDataProvider<NBPExchangeRatesTablePOJO[]> dataProvider;

	@Autowired
	private ExchangeRatesService exchangeRatesTableService;

	@BeforeEach
	private void init() {
//		when(iExchangeRatesTableRepository
//				.findFirstByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo))
//						.thenReturn(highestValueInDates);
	}

	@Test
	public void getHighestExchangeRatesTable() {
//		BigDecimal exchangeRate = exchangeRatesTableService.getHighestExchangeRatesTable(currencyCode, dateFrom, dateTo);
//		assertEquals(highestExchangeRate, exchangeRate);
	}
}
