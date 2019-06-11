package com.challenge.n26.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.n26.exception.FutureTransactionException;
import com.challenge.n26.exception.InvalidInputException;
import com.challenge.n26.exception.OlderTransactionException;
import com.challenge.n26.model.Statistics;
import com.challenge.n26.model.Transaction;
import com.challenge.n26.util.Util;
import com.challenge.n26.validator.Validator;

@Service
public class TransactionService {

	@Autowired
	private TransactionManager transactionManager;

	public void addTransaction(final Transaction transaction)
			throws OlderTransactionException, FutureTransactionException, InvalidInputException {
		long timestamp = Util.toEpochMilli(transaction.getTimestamp());
		if (Validator.isTransactionOlder(timestamp, Instant.now().toEpochMilli())) {
			throw new OlderTransactionException("Input transaction timestamp is older");
		}
		if (Validator.isTransactionInFuture(timestamp, Instant.now().toEpochMilli())) {
			throw new FutureTransactionException("Input transaction timestamp is in future");
		}
		transactionManager.addTransaction(transaction);
	}

	public void deleteTransactions() throws OlderTransactionException {
		transactionManager.deleteTransactions();
	}

	public Statistics getStatistics() {
		long currentTime = Instant.now().toEpochMilli();
		return transactionManager.getStatistics(currentTime);
	}

}
