package com.djedra.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumUtil {

	public static <T extends IEnumType<E>, E> T getEnumByValue(String fieldName, E value, Class<T> e) {
		return Stream.of(e.getEnumConstants()).filter(v -> v.getValue().equals(value)).findAny()
				.orElseThrow(() -> new RuntimeException(
						String.format("Wrong parameter value passed as for: %s, [value: %s]. Acceptable values: [%s]",
								fieldName, value,
								Stream.of(e.getEnumConstants()).map(v -> v.getValue()).collect(Collectors.toList()))));
	}
}
