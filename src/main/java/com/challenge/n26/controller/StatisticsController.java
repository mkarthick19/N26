package com.challenge.n26.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.n26.model.Statistics;
import com.challenge.n26.service.TransactionService;

@RestController
public class StatisticsController {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

	@Autowired
	private TransactionService transactionService;

	/**
	 * REST endpoint for getting the transaction statistics
	 *
	 * @return TransactionStatistics
	 */
	@GetMapping("/statistics")
	public Statistics getStatistics() {
		return transactionService.getStatistics();
	}
}
