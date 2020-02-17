package com.djedra.util;

import java.io.IOException;

@FunctionalInterface
public interface FunctionWithThrows<R, T> {

	T apply(R t) throws IOException;
}
