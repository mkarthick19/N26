package com.challenge.n26.exception;

public class OlderTransactionException extends Exception {

	public OlderTransactionException() {}
	
	public OlderTransactionException(String message) {
		super(message);
	}
}
