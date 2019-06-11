package com.challenge.n26.controller;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.n26.exception.FutureTransactionException;
import com.challenge.n26.exception.InvalidInputException;
import com.challenge.n26.exception.OlderTransactionException;
import com.challenge.n26.model.Transaction;
import com.challenge.n26.service.TransactionService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestController
public class TransactionsController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionsController.class);

	@Autowired
	private TransactionService transactionService;

	/**
	 * REST endpoint for adding a transaction
	 *
	 * @param InputRequest
	 * @return ResponseEntity<>
	 */
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<?> addTransaction(@RequestBody @NotNull Transaction transaction) {
		try {
			transactionService.addTransaction(transaction);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (OlderTransactionException ex) {
			logger.error("Older Transaction Exception - Input Transaction Timestamp is older", ex);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (FutureTransactionException | InvalidInputException ex) {
			logger.error("Future Transaction Exception - Input Transaction Timestamp is in future.", ex);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception ex) {
			logger.error("Common Exception - Error in posting the transactions.", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * REST endpoint for deleting all transactions
	 *
	 * @return ResponseEntity<>
	 */
	@RequestMapping(value = "/transactions", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTransactions(@RequestBody(required = false) Transaction transaction) {
		try {
			transactionService.deleteTransactions();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception ex) {
			logger.error("Common Exception - Error in posting the transactions.", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler({ InvalidFormatException.class })
	public ResponseEntity<?> handleException() {
		return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
