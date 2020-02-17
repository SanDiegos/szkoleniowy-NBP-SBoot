package com.djedra.repository;

import com.djedra.connection.IConnection;
import com.djedra.entity.IEntityHead;
import com.djedra.parser.IParser;

public class CurrencyRepository {

	public <S, D extends IEntityHead> D makeRequest(IParser<S, D> parser, IConnection<S> connection) {
		return parser.parse(connection.downloadData());
	}

}
