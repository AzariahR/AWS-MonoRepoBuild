package com.azar.fsdemo.statemachine.lambda.input;

import java.io.Serializable;

public class BatchJobDetails implements Serializable {
	private static final long serialVersionUID = -3308078896838522842L;

	private String jobName;
	private String jobDefinitionName;
	private int totalRecordsCount;
	private int numOfChunks;
	private int chunkSize;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobDefinitionName() {
		return jobDefinitionName;
	}

	public void setJobDefinitionName(String jobDefinitionName) {
		this.jobDefinitionName = jobDefinitionName;
	}

	public int getTotalRecordsCount() {
		return totalRecordsCount;
	}

	public void setTotalRecordsCount(int totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}

	public int getNumOfChunks() {
		return numOfChunks;
	}

	public void setNumOfChunks(int numOfChunks) {
		this.numOfChunks = numOfChunks;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

}