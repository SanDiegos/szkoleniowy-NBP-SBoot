package com.djedra.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rates")
public class ControllerArgumentsValidator {

	public static void checkIfDateIsPastOrPresent(LocalDate date) {
		if (date.isAfter(LocalDate.now())) {
			throw new RuntimeException("You cannot search for course currency in future.");
		}
	}
}
