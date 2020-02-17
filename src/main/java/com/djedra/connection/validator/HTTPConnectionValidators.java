package com.djedra.connection.validator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

import com.djedra.exception.ConnectionException;
import com.djedra.util.FunctionWithThrows;

public class HTTPConnectionValidators {

	public static boolean validateConnectionWithoutThrow(HttpURLConnection connection) {
		Optional<Integer> responseCode = checkResponse(connection, HttpURLConnection::getResponseCode);
		if (responseCode.get() != HttpURLConnection.HTTP_OK) {
			System.err.println(String.format("Didn't find data in URL: [%s].", connection.getURL().getPath()));
			return false;
		}
		return true;
	}

	public static boolean validateConnection(HttpURLConnection connection) {
		Optional<Integer> responseCode = checkResponse(connection, HttpURLConnection::getResponseCode);
		Optional<String> responseMessage = checkResponse(connection, HttpURLConnection::getResponseMessage);
		if (responseCode.get() != HttpURLConnection.HTTP_OK) {
			throw new ConnectionException(String.format(
					"Bad HTTP status! [%d], Error message: [%s]. Probably wrong URL adress was used. Check the URL.",
					responseCode.get(), responseMessage.get()));
		}
		return true;
	}

	private static <T> Optional<T> checkResponse(HttpURLConnection connection,
			FunctionWithThrows<HttpURLConnection, T> getter) {
		Optional<T> response = null;
		try {
			response = Optional.of(getter.apply(connection));
		} catch (IOException e) {
			System.out
					.println(String.format("Error while getting data from URL: [%s].", connection.getURL().getPath()));
		}
		return response;
	}

}
