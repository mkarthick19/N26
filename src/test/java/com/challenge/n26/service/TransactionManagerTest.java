package com.challenge.n26.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.challenge.n26.exception.InvalidInputException;
import com.challenge.n26.exception.OlderTransactionException;
import com.challenge.n26.model.Statistics;
import com.challenge.n26.model.Transaction;

public class TransactionManagerTest {

	private TransactionManager transactionManager;

	private Transaction transaction1, transaction2;

	private String bigDecimal1, bigDecimal2, bigDecimal3, bigDecimal4, bigDecimal5;

	@Before
	public void setup() {
		transactionManager = new TransactionManager();
		transaction1 = new Transaction(10L, "2018-07-17T09:59:51.312Z");
		transaction2 = new Transaction(20L, "2018-07-17T09:59:50.31Z");
		bigDecimal1 = new BigDecimal("10.00").toString();
		bigDecimal2 = new BigDecimal("15.00").toString();
		bigDecimal3 = new BigDecimal("20.00").toString();
		bigDecimal4 = new BigDecimal("30.00").toString();
		bigDecimal5 = new BigDecimal("0.00").toString();
	}

	@Test
	public void testGetStatisticsWithTransactions() throws OlderTransactionException, InvalidInputException {
		transactionManager.addTransaction(transaction1);
		transactionManager.addTransaction(transaction2);
		Statistics statistics = transactionManager.getStatistics(1531821591312L);
		assertEquals(2, statistics.getCount());
		assertTrue(bigDecimal1.compareTo(statistics.getMin()) == 0);
		assertTrue(bigDecimal3.compareTo(statistics.getMax()) == 0);
		assertTrue(bigDecimal2.compareTo(statistics.getAvg()) == 0);
		assertTrue(bigDecimal4.compareTo(statistics.getSum()) == 0);
	}

	@Test
	public void testGetStatisticsWithoutTransactions() throws OlderTransactionException {
		Statistics statistics = transactionManager.getStatistics(1531821591312L);
		assertEquals(0, statistics.getCount());
		assertTrue(bigDecimal5.compareTo(statistics.getAvg()) == 0);
		assertTrue(bigDecimal5.compareTo(statistics.getSum()) == 0);
	}

	@Test
	public void testDeleteTransactions() throws OlderTransactionException, InvalidInputException {
		transactionManager.addTransaction(transaction1);
		transactionManager.addTransaction(transaction2);
		transactionManager.deleteTransactions();
		Statistics statistics = transactionManager.getStatistics(1531821591312L);
		assertEquals(0, statistics.getCount());
	}
}
