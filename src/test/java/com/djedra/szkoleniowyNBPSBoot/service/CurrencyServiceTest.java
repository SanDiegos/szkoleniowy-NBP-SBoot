package com.djedra.szkoleniowyNBPSBoot.service;

import static org.junit.jupiter.api.Assertions.assertAll;
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
import com.djedra.entity.currency.Currency;
import com.djedra.entity.currency.Rate;
import com.djedra.repository.currency.ICurrencyRepository;
import com.djedra.repository.currency.IRateRepository;
import com.djedra.service.currency.CurrencyService;
import com.djedra.szkoleniowyNBPSBoot.controller.CurrencyDataFactory;
import com.djedra.util.Constants.ExchangeRateTableTypes;
import com.djedra.util.EnumUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CurrencyServiceTest {

	private final Rate validRate = new Rate(2L, null, CurrencyDataFactory.VALID_EFFECTIVE_DATA, BigDecimal.valueOf(11.11),
			"no2", BigDecimal.valueOf(22.22), BigDecimal.valueOf(33.33));
	private final Currency currencyValidDataIDB = CurrencyDataFactory.createValidData(Arrays.asList(validRate));


	private final LocalDate weekend = LocalDate.of(2020, 02, 16);
	private final Rate NBPRate = new Rate(3L, null, weekend.minusDays(2), BigDecimal.valueOf(11.11), "notInDB2",
			BigDecimal.valueOf(222.222), BigDecimal.valueOf(333.333));
	private final Currency currencyValidDataNotInDB = new Currency("A", "Dolar", "USD", Arrays.asList(NBPRate));

	@MockBean
	private ICurrencyRepository currencyRepository;
	@MockBean
	private IRateRepository rateRepository;
	@MockBean
	private IDataProvider<Currency> dataProvider;
	@Autowired
	private CurrencyService currencyService;
	
	@BeforeEach
	private void init() {
		when(currencyRepository.findCurrencyBytableTypeAndCodeAndRates_effectiveDate(
				currencyValidDataIDB.getTableType(), currencyValidDataIDB.getCode(),
				CurrencyDataFactory.VALID_EFFECTIVE_DATA)).thenReturn(currencyValidDataIDB);
		when(currencyRepository.findCurrencyBytableTypeAndCodeAndRates_effectiveDate(
				currencyValidDataNotInDB.getTableType(), currencyValidDataNotInDB.getCode(),
				weekend)).thenReturn(currencyValidDataNotInDB);
	}

	@Test
	public void getExchangeRateForDateReturnFromDB() throws Exception {

		ExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", currencyValidDataIDB.getTableType(),
				ExchangeRateTableTypes.class);

		Currency exchangeRateForDate = currencyService.getExchangeRateForDate(tabType, currencyValidDataIDB.getCode(),
				CurrencyDataFactory.VALID_EFFECTIVE_DATA);

		assertAll(() -> {
			assertEquals(currencyValidDataIDB, exchangeRateForDate);
			assertEquals(validRate, currencyValidDataIDB.getRates().get(0));
		});
	}

	@Test
	public void getExchangeRateForDateReturnTwoDaysEalier() throws Exception {

		ExchangeRateTableTypes tabType = EnumUtil.getEnumByValue("tableType", currencyValidDataNotInDB.getTableType(),
				ExchangeRateTableTypes.class);

		Currency exchangeRateForDate = currencyService.getExchangeRateForDate(tabType,
				currencyValidDataNotInDB.getCode(), weekend);

		assertAll(() -> {
			assertEquals(currencyValidDataNotInDB, exchangeRateForDate);
			assertEquals(NBPRate, currencyValidDataNotInDB.getRates().get(0));
		});
	}
}

