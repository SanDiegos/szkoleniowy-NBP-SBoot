package com.djedra.parser;

import com.djedra.entity.IEntityHead;

public interface IParser<S, D extends IEntityHead> {

	D parse(S data);
}
