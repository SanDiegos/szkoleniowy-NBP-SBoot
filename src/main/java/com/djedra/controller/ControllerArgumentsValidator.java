package com.djedra.controller;

import java.time.LocalDate;

import javax.validation.ValidationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rates")
public class ControllerArgumentsValidator {

	public static void checkIfDateIsPastOrPresent(LocalDate date) {
		if (date.isAfter(LocalDate.now())) {
			throw new ValidationException("You cannot search for course currency in future.");
		}
	}

	public static void checkIfDateFromIsBeforeDateTo(LocalDate dateFrom, LocalDate dateTo) {
		if (dateFrom.isAfter(dateTo)) {
			throw new ValidationException("Wrong dates \"from\" and \"to\".");
		}
	}
}
