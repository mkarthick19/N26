package com.challenge.n26.validator;

import org.springframework.stereotype.Component;

import com.challenge.n26.constants.Constants;
import com.challenge.n26.exception.OlderTransactionException;

@Component
public class Validator {

	public static boolean isTransactionOlder(final long timestamp, final long currentTimestamp) {
		return timestamp < (currentTimestamp - Constants.MAX_INTERVAL + Constants.INTERVAL);
	}

	public static boolean isTransactionInFuture(final long timestamp, final long currentTimestamp) {
		return timestamp > currentTimestamp;
	}	
}
