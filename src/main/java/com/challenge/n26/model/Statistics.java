package com.challenge.n26.model;

import java.math.BigDecimal;

public class Statistics {

	private String sum;

	private String avg;

	private String max;

	private String min;

	private long count;

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getAvg() {
		return avg;
	}

	public void setAvg(String avg) {
		this.avg = avg;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Statistics(String sum, String avg, String max, String min, long count) {
		super();
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}

	public Statistics() {
		this.sum = new BigDecimal("0.00").toString();
		this.avg = new BigDecimal("0.00").toString();
		this.max = new BigDecimal("0.00").toString();
		this.min = new BigDecimal("0.00").toString();
		this.count = 0;
	}
}
