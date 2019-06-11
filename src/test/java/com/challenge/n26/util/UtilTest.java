package com.challenge.n26.util;

import static org.junit.Assert.*;

import java.time.format.DateTimeParseException;

import org.junit.Test;

import com.challenge.n26.exception.InvalidInputException;

public class UtilTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullInput() throws InvalidInputException {
		Util.toEpochMilli(null);
	}

	@Test
	public void testHappyCase() throws InvalidInputException {
		assertEquals(1531821591312L, Util.toEpochMilli("2018-07-17T09:59:51.312Z"));
	}

	@Test(expected = InvalidInputException.class)
	public void testInvalidInput() throws InvalidInputException {
		Util.toEpochMilli("Hello59:51.312Z");
	}
}
