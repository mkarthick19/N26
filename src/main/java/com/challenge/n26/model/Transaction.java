package com.challenge.n26.model;

public class Transaction {

	private double amount;
	
	private String timestamp;
	
	public Transaction(double amount, String timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
