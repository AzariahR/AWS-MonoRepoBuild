package com.azar.fsdemo.batchdata.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClient;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.google.gson.JsonObject;

@RestController
public class BatchDataProviderController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "startBatchJob", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> startBatchJob() throws Exception {
		logger.info("Start Batch Job...");

		// construct input data JSON
		int totalRecordsCount = 1387;

		int chunkSize = 1000;

		int numOfChunks = (totalRecordsCount - (totalRecordsCount % chunkSize)) / chunkSize;

		if (totalRecordsCount % chunkSize > 0) {
			numOfChunks++;
		}

		JsonObject parentObj = new JsonObject();
		parentObj.addProperty("jobDefinitionName", "first-run-job-definition:5");
		parentObj.addProperty("jobName", "MorningJob");
		parentObj.addProperty("totalRecordsCount", totalRecordsCount);
		parentObj.addProperty("numOfChunks", numOfChunks);
		parentObj.addProperty("chunkSize", chunkSize);

		// construct execution request
		StartExecutionRequest req = new StartExecutionRequest()
				.withStateMachineArn("arn:aws:states:us-west-1:391566720493:stateMachine:BatchJobWorkFlow")
				.withInput(parentObj.toString());

		// construct credentials - TODO: env creds
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("ACCESS_KEY_ID",
				"ACCESS_SECRET_KEY");

		// trigger the step fucntion execution
		AWSStepFunctionsClient.builder().withRegion("us-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build().startExecution(req);

		return new ResponseEntity<String>("Batch Job Triggered Successfully", HttpStatus.OK);
	}

	@RequestMapping(value = "batchdata", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<BatchData>> getBatchData(@RequestParam("startIdx") int startIdx,
			@RequestParam("endIdx") int endIdx) throws Exception {
		logger.info("Generating Batch Data..");
		List<BatchData> data = new ArrayList<>();

		IntStream.range(startIdx, endIdx).forEach(idx -> {
			double val = Math.random() * 27 * 10000;
			BatchData dt = new BatchData(idx, "name" + idx, BigInteger.valueOf((long) val));
			data.add(dt);
		});

		return new ResponseEntity<List<BatchData>>(data, HttpStatus.OK);
	}
}
