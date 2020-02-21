package com.djedra.szkoleniowyNBPSBoot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

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
import com.djedra.entity.exchangeratestable.ExchangeRatesTable;
import com.djedra.entity.exchangeratestable.Rates;
import com.djedra.repository.exchangeratestable.IExchangeRatesTableRepository;
import com.djedra.repository.exchangeratestable.IRatesRepository;
import com.djedra.service.exchangeratestable.ExchangeRatesTableService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ExchangeRatesTableServiceTest {

	private BigDecimal highestExchangeRate = BigDecimal.valueOf(3);
	private String currencyCode = "currCode";
	private String currency = "currency";
	private LocalDate dateFrom = LocalDate.of(2020, 02, 12);
	private LocalDate dateTo = LocalDate.of(2020, 02, 15);
	private Rates rates = new Rates(1L, currencyCode, currency, highestExchangeRate, null);
	private ExchangeRatesTable highestValueInDates = new ExchangeRatesTable(1L, "A", dateFrom, "no",
			Arrays.asList(rates));

	@MockBean
	private IDataProvider<ExchangeRatesTable[]> dataProvider;
	@MockBean
	private IExchangeRatesTableRepository iExchangeRatesTableRepository;
	@MockBean
	private IRatesRepository iRatesRepository;

	@Autowired
	private ExchangeRatesTableService exchangeRatesTableService;

	@BeforeEach
	private void init() {
		when(iExchangeRatesTableRepository
				.findFirstByRates_CodeAndEffectiveDateBetweenOrderByRates_rateDesc(currencyCode, dateFrom, dateTo))
						.thenReturn(highestValueInDates);
	}

	@Test
	public void getHighestExchangeRatesTable() {
		BigDecimal exchangeRate = exchangeRatesTableService.getHighestExchangeRatesTable(currencyCode, dateFrom, dateTo);
		assertEquals(highestExchangeRate, exchangeRate);
	}
}
