package com.djedra.connection;

public interface IConnection<C> {

	boolean validateConnection();

	C downloadData();
}
