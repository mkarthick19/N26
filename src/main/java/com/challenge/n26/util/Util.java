package com.challenge.n26.util;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import com.challenge.n26.exception.InvalidInputException;

public final class Util {

	public static long toEpochMilli(String timestamp) throws InvalidInputException {
		try {
			if (timestamp == null || timestamp.length() == 0) {
				throw new IllegalArgumentException("timestamp cannot be null:");
			}
			Instant instant = Instant.parse(timestamp);
			return instant.toEpochMilli();
		} catch (DateTimeParseException ex) {
			throw new InvalidInputException();
		}
	}
}
