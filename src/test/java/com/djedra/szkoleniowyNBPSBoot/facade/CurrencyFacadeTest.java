package com.djedra.szkoleniowyNBPSBoot.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.djedra.entity.Country;
import com.djedra.entity.Currency;
import com.djedra.entity.CurrencyToCountry;
import com.djedra.entity.Rate;
import com.djedra.facade.CurrencyFacade;
import com.djedra.service.currency.CurrencyService;
import com.djedra.service.exchangerates.ExchangeRatesService;
import com.djedra.util.Constants.ExchangeRateTableTypes;
import com.djedra.util.EnumUtil;

@WebMvcTest(CurrencyFacade.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CurrencyFacadeTest {

	private ExchangeRateTableTypes tableType = ExchangeRateTableTypes.A;
	private ExchangeRateTableTypes tableTypeC = ExchangeRateTableTypes.C;
	private String wrongTableType = "WrongTableType";
	private String currencyCode = "curr";
	private String wrongCurrencyCode = "currNN";
	private LocalDate date = LocalDate.of(2020, 02, 20);

	private BigDecimal mid = BigDecimal.valueOf(11);
	private Rate rate = new Rate(date, mid);
	private Country country = new Country("country");
	private CurrencyToCountry currencyToCountry = new CurrencyToCountry(null, country);
	private Currency currency = new Currency(1L, currencyCode, Arrays.asList(rate), Arrays.asList(currencyToCountry));

	@Autowired
	private CurrencyFacade currencyFacade;
	@MockBean
	private CurrencyService currencyService;
	@MockBean
	private ExchangeRatesService exchangeRatesTableService;

	@BeforeEach
	private void init() {
		when(currencyService.getCurrentExchangeRate(tableType, currencyCode)).thenReturn(currency);
		when(currencyService.getCurrentExchangeRate(tableTypeC, wrongCurrencyCode)).thenReturn(null);
	}

	@Test
	public void exchange_return_valid_data() {
		BigDecimal returnedData = currencyFacade.exchange(tableType.getValue(), currencyCode, BigDecimal.TEN);
		assertEquals(mid.multiply(BigDecimal.TEN), returnedData);
	}

	@Test
	public void exchange_data_not_found_throw_runtime_exception() {
		Exception exception = assertThrows(RuntimeException.class,
				() -> currencyFacade.exchange(tableTypeC.getValue(), wrongCurrencyCode, BigDecimal.TEN));

		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains("Didn't found current course currency."));
	}

	@Test
	public void exchange_throw_runtime_exception() {
		Exception exception = assertThrows(RuntimeException.class,
				() -> currencyFacade.exchange(wrongTableType, currencyCode, BigDecimal.TEN));

		String actualMessage = exception.getMessage();
		String expectedMessage = null;
		try {
			EnumUtil.getEnumByValue("tableType", wrongTableType, ExchangeRateTableTypes.class);
		} catch (RuntimeException e) {
			expectedMessage = e.getMessage();
		}
		assertTrue(actualMessage.contains(expectedMessage));
	}
}
