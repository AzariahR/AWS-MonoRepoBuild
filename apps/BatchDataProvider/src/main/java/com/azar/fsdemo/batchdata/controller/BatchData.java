package com.azar.fsdemo.batchdata.controller;

import java.io.Serializable;
import java.math.BigInteger;

public class BatchData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer loanId;
	private String name;
	private BigInteger loanAmount;

	public BatchData(Integer loanId, String name, BigInteger loanAmount) {
		this.loanId = loanId;
		this.name = name;
		this.loanAmount = loanAmount;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigInteger loanAmount) {
		this.loanAmount = loanAmount;
	}
}
