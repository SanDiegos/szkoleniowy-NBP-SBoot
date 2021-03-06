package com.djedra.connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.djedra.exception.ConnectionException;
import com.djedra.util.Constants;

public class FileDataProvider implements IDataProvider<String> {
	public FileDataProvider() {
	}

	@Override
	public boolean hasData(HashMap params) {
		File file = new File(Constants.FILE_PATH);
		if (!file.exists()) {
			throw new ConnectionException(String.format("File doesn't exsists on directory: [%s]", file.getPath()));
		}
		return true;
	}

	@Override
	public String downloadData(HashMap params) {
		Path path = Paths.get(Constants.FILE_PATH);
		String data;
		try {
			data = Files.readAllLines(path).get(0);
//			wyci�ga� z pliku po linii i sprawdza� czy jest dla tych parametrow
		} catch (FileNotFoundException e) {
			throw new ConnectionException("Error while reading the file.");
		} catch (IOException e) {
			throw new ConnectionException("Error while reading the file.");
		}

		return data;
	}
}
