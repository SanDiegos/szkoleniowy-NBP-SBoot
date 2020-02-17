package com.djedra.connection;

import java.io.File;

import com.djedra.exception.ConnectionException;



public class FileConnection implements IConnection<File> {

	private final File file;

	public FileConnection(IPath<String> path) {
		this.file = new File(path.getPath());
	}

	@Override
	public boolean validateConnection() {
		if (!file.exists()) {
			throw new ConnectionException(String.format("File doesn't exsists on directory: [%s]", file.getPath()));
		}
		return true;
	}

	@Override
	public File downloadData() {
		return file;
	}
}
