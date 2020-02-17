package com.djedra.controller;

import java.time.LocalDate;

public class ControllerArgumentsValidator {

	public static void checkIfDateIsPastOrPresent(LocalDate date) {
		if (date.isAfter(LocalDate.now())) {
			throw new RuntimeException("You cannot search for course currency in future.");
		}
	}
}
