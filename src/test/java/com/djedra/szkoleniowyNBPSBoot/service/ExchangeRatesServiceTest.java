package com.djedra.szkoleniowyNBPSBoot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
import com.djedra.entity.Country;
import com.djedra.entity.Rate;
import com.djedra.exception.ExchangeRatesServiceException;
import com.djedra.nbpexchangeratestablepojo.exchangeratestable.NBPExchangeRatesTablePOJO;
import com.djedra.repository.ICurrencyRepository;
import com.djedra.repository.ICurrencyToCountryRepository;
import com.djedra.repository.IRateRepository;
import com.djedra.service.exchangerates.ExchangeRatesService;
import com.djedra.util.Constants.ExchangeRatesTableTypes;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ExchangeRatesServiceTest {

	private BigDecimal highestExchangeRate = BigDecimal.valueOf(777);
	private String currencyCode = "currCode";
	private LocalDate dateFrom = LocalDate.of(2020, 02, 01);
	private LocalDate dateTo = LocalDate.of(2020, 02, 15);
	private Rate rate = new Rate(dateFrom, highestExchangeRate);
	private List<Country> countriesWithMoreThanOneCurrency = Arrays.asList(new Country("Country"));
	private List<Rate> top5 = Arrays.asList(new Rate(dateFrom, highestExchangeRate),
			new Rate(dateFrom.plusDays(1), highestExchangeRate.add(BigDecimal.ONE)),
			new Rate(dateFrom.plusDays(2), highestExchangeRate.add(BigDecimal.valueOf(2))),
			new Rate(dateFrom.plusDays(3), highestExchangeRate.add(BigDecimal.valueOf(3))),
			new Rate(dateFrom.plusDays(4), highestExchangeRate.add(BigDecimal.valueOf(4))));

	private List<Rate> low5 = Arrays.asList(new Rate(dateTo, highestExchangeRate.subtract(BigDecimal.ONE)),
			new Rate(dateTo.minusDays(2), highestExchangeRate.subtract(BigDecimal.valueOf(2))),
			new Rate(dateTo.minusDays(3), highestExchangeRate.subtract(BigDecimal.valueOf(3))),
			new Rate(dateTo.minusDays(4), highestExchangeRate.subtract(BigDecimal.valueOf(4))),
			new Rate(dateTo.minusDays(5), highestExchangeRate.subtract(BigDecimal.valueOf(5))));

	@MockBean
	private IDataProvider<NBPExchangeRatesTablePOJO[]> dataProvider;
	@MockBean
	private IRateRepository rateRepository;
	@MockBean
	private ICurrencyRepository currencyRepository;
	@MockBean
	private ICurrencyToCountryRepository currencyToCountryRepository;

	@Autowired
	private ExchangeRatesService exchangeRatesTableService;

	@BeforeEach
	private void init() {
		when(rateRepository.findFirstByCurrency_CodeAndDateBetweenOrderByMidDesc(currencyCode, dateFrom, dateTo))
				.thenReturn(rate);
		when(currencyToCountryRepository.findCountryHavingMoreThanOneCurrency())
				.thenReturn(countriesWithMoreThanOneCurrency);
		when(rateRepository.findTop5ByCurrency_CodeAndDateBetweenOrderByMidDesc(currencyCode, dateFrom, dateTo))
				.thenReturn(top5);
		when(rateRepository.findTop5ByCurrency_CodeAndDateBetweenOrderByMidAsc(currencyCode, dateFrom, dateTo))
				.thenReturn(low5);
	}

	@Test
	public void get_highest_exchange_rates_table_return_valid_data() {
		Rate returnedData = exchangeRatesTableService.getHighestExchangeRatesTable(currencyCode, dateFrom, dateTo);
		assertEquals(returnedData, rate);
	}

	@Test
	public void get_country_having_more_than_one_currency_return_valid_data() {
		List<Country> returnedData = exchangeRatesTableService.getCountryHavingMoreThanOneCurrency();
		assertEquals(returnedData, countriesWithMoreThanOneCurrency);
	}

	@Test
	public void get_five_highest_or_lowest_currency_course_return_valid_data() {
		List<Rate> returnedDataTop5 = exchangeRatesTableService
				.getFiveHighestOrLowestCurrencyCourse(ExchangeRatesTableTypes.A, currencyCode, dateFrom, dateTo, true);
		List<Rate> returnedDataLow5 = exchangeRatesTableService
				.getFiveHighestOrLowestCurrencyCourse(ExchangeRatesTableTypes.A, currencyCode, dateFrom, dateTo, false);
		assertEquals(returnedDataTop5, top5);
		assertEquals(returnedDataLow5, low5);
	}

	@Test
	public void get_currency_with_highest_deffrence_between_dates() {
		when(currencyRepository.findHighestCurrencyCourseDeffrenceBetweenDates()).thenReturn(null);
		Exception exception = assertThrows(ExchangeRatesServiceException.class,
				() -> exchangeRatesTableService.getCurrencyWithHighestDiffrenceBetweenDates(dateFrom, dateTo));

		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains("Data not found"));

	}
}
