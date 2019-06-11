package com.challenge.n26.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.challenge.n26.constants.Constants;
import com.challenge.n26.exception.InvalidInputException;
import com.challenge.n26.exception.OlderTransactionException;
import com.challenge.n26.model.Statistics;
import com.challenge.n26.model.Transaction;
import com.challenge.n26.model.TransactionStatistics;
import com.challenge.n26.util.Util;
import com.challenge.n26.validator.Validator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class TransactionManager {

	private final TransactionStatistics[] transactionStatistics;

	private int transactionStatisticsSize = 0;

	private ReadWriteLock lock;

	public TransactionManager() {
		transactionStatisticsSize = (int) (Constants.MAX_INTERVAL / Constants.INTERVAL);
		transactionStatistics = new TransactionStatistics[transactionStatisticsSize];
		this.lock = new ReentrantReadWriteLock();
		initialize();
	}

	private void initialize() {
		for (int i = 0; i < transactionStatistics.length; i++) {
			transactionStatistics[i] = new TransactionStatistics();
		}
	}

	private ReadWriteLock getLock() {
		return lock;
	}

	public Statistics getStatistics(final long currentTime) {
		try {
			getLock().readLock().lock();
			List<TransactionStatistics> validTransactionStatistics = getAllTransactionStatistics(currentTime);
			TransactionStatistics finalTransactionStatistics = new TransactionStatistics();
			if (validTransactionStatistics.size() == 0) {
				return new Statistics();
			}
			for (TransactionStatistics transactionStatistics : validTransactionStatistics) {
				finalTransactionStatistics = merge(finalTransactionStatistics, transactionStatistics);
			}
			BigDecimal statsCount = new BigDecimal(finalTransactionStatistics.getCount());
			if (statsCount.compareTo(BigDecimal.ZERO) != 0) {
				finalTransactionStatistics
						.setAvg(finalTransactionStatistics.getSum().divide(statsCount, 2, RoundingMode.HALF_UP));
			}
			finalTransactionStatistics
					.setSum(finalTransactionStatistics.getSum().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			finalTransactionStatistics
					.setAvg(finalTransactionStatistics.getAvg().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			finalTransactionStatistics
					.setMin(finalTransactionStatistics.getMin().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			finalTransactionStatistics
					.setMax(finalTransactionStatistics.getMax().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			Statistics result = new Statistics(finalTransactionStatistics.getSum().toString(),
					finalTransactionStatistics.getAvg().toString(), finalTransactionStatistics.getMax().toString(),
					finalTransactionStatistics.getMin().toString(), finalTransactionStatistics.getCount());
			return result;
		} finally {
			getLock().readLock().unlock();
		}
	}

	public void addTransaction(final Transaction transaction) throws OlderTransactionException, InvalidInputException {
		try {
			getLock().readLock().lock();
			int transactionIndex = getTransactionIndexForTimestamp(Util.toEpochMilli(transaction.getTimestamp()));
			if (transactionStatistics[transactionIndex].getCount() == 0) {
				transactionStatistics[transactionIndex] = create(transactionIndex, transaction);
			} else {
				if (Validator.isTransactionOlder(transactionStatistics[transactionIndex].getTimestamp(),
						Instant.now().toEpochMilli())) {
					transactionStatistics[transactionIndex].reset();
					transactionStatistics[transactionIndex] = create(transactionIndex, transaction);
				} else {
					transactionStatistics[transactionIndex] = merge(transactionIndex, transaction);
				}
			}
		} finally {
			getLock().readLock().unlock();
		}
	}

	public void deleteTransactions() {
		try {
			getLock().readLock().lock();
			initialize();
		} finally {
			getLock().readLock().unlock();
		}
	}

	private TransactionStatistics create(int transactionIndex, final Transaction transaction) throws InvalidInputException {
		BigDecimal amount = new BigDecimal(transaction.getAmount());
		transactionStatistics[transactionIndex].setMin(amount);
		transactionStatistics[transactionIndex].setMax(amount);
		transactionStatistics[transactionIndex].setCount(1);
		transactionStatistics[transactionIndex].setAvg(amount);
		transactionStatistics[transactionIndex].setSum(amount);
		transactionStatistics[transactionIndex].setTimestamp(Util.toEpochMilli(transaction.getTimestamp()));
		return transactionStatistics[transactionIndex];
	}

	private TransactionStatistics merge(int transactionIndex, final Transaction transaction) throws InvalidInputException {
		try {
			getLock().readLock().lock();
			BigDecimal amount = new BigDecimal(transaction.getAmount());
			transactionStatistics[transactionIndex]
					.setSum(transactionStatistics[transactionIndex].getSum().add(amount));
			transactionStatistics[transactionIndex].setCount(transactionStatistics[transactionIndex].getCount() + 1);
			transactionStatistics[transactionIndex]
					.setMin(transactionStatistics[transactionIndex].getMin().min(amount));
			transactionStatistics[transactionIndex]
					.setMax(transactionStatistics[transactionIndex].getMax().max(amount));
			transactionStatistics[transactionIndex].setTimestamp(Util.toEpochMilli(transaction.getTimestamp()));
			return transactionStatistics[transactionIndex];
		} finally {
			getLock().readLock().unlock();
		}
	}

	private TransactionStatistics merge(TransactionStatistics finalTransactionStatistics,
			TransactionStatistics transactionStatistics) {
		try {
			getLock().readLock().lock();
			finalTransactionStatistics.setSum(finalTransactionStatistics.getSum().add(transactionStatistics.getSum()));
			finalTransactionStatistics
					.setCount(finalTransactionStatistics.getCount() + transactionStatistics.getCount());
			BigDecimal statsCount = new BigDecimal(finalTransactionStatistics.getCount());
			if (statsCount.compareTo(BigDecimal.ZERO) != 0) {
				finalTransactionStatistics
						.setAvg(finalTransactionStatistics.getSum().divide(statsCount, 2, RoundingMode.HALF_UP));
			}
			finalTransactionStatistics.setMin(finalTransactionStatistics.getMin().min(transactionStatistics.getMin()));
			finalTransactionStatistics.setMax(finalTransactionStatistics.getMax().max(transactionStatistics.getMax()));
			return finalTransactionStatistics;
		} finally {
			getLock().readLock().unlock();
		}
	}

	private int getTransactionIndexForTimestamp(final long timestamp) {
		return (int) ((timestamp / Constants.INTERVAL) % (Constants.MAX_INTERVAL / Constants.INTERVAL));
	}

	private List<TransactionStatistics> getAllTransactionStatistics(long currentTimestamp) {
		List<TransactionStatistics> transactionStatisticsList = new ArrayList<>();
		for (int i = 0; i < transactionStatisticsSize; i++) {
			if (!Validator.isTransactionOlder(transactionStatistics[i].getTimestamp(), currentTimestamp)) {
				transactionStatisticsList.add(transactionStatistics[i]);
			}
		}
		return transactionStatisticsList;
	}
}
