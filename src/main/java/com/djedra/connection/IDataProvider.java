package com.djedra.connection;

import java.util.HashMap;

public interface IDataProvider<C> {

	C downloadData(HashMap<String, Object> params);

	boolean hasData(HashMap<String, Object> params);
}
