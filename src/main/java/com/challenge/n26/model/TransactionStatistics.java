package com.challenge.n26.model;

import java.math.BigDecimal;

public class TransactionStatistics {

	private BigDecimal sum;

	private BigDecimal avg;
	
	private BigDecimal max;
	
	private BigDecimal min;

	private long count;
	
	private long timestamp;

	public TransactionStatistics() {
		reset();
	}
	
	public void reset() {
		this.sum = new BigDecimal("0");
		this.avg = new BigDecimal("0");
		this.max = BigDecimal.valueOf(Double.MIN_VALUE);
		this.min = BigDecimal.valueOf(Double.MAX_VALUE);
		this.count = 0;		
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
