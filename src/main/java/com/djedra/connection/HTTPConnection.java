package com.djedra.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

import com.djedra.exception.ConnectionException;


public class HTTPConnection implements IConnection<String> {

	private final HttpURLConnection connection;
	private final Function<HttpURLConnection, Boolean> httpConnectionValidator;

	public HTTPConnection(IPath<URL> url, Function<HttpURLConnection, Boolean> validation) {
		this.connection = httpURLConnectionCreator(url.getPath());
		this.httpConnectionValidator = validation;
	}

	@Override
	public String downloadData() {
		Optional<String> response = null;
		try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			response = responseReader.lines().findAny();
		} catch (IOException e) {
			throw new ConnectionException(String.format("Error while reading response form connection on URL: [%s].",
					connection.getURL().getPath()), e);
		} finally {
			connection.disconnect();
		}
		return response.get();
	}

	@Override
	public boolean validateConnection() {
		return httpConnectionValidator.apply(connection);
	}

	private HttpURLConnection httpURLConnectionCreator(URL u) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) u.openConnection();
		} catch (IOException e) {
			throw new ConnectionException("Error while trying to open connection via HTTP.", e);
		}
		return connection;
	}

}
