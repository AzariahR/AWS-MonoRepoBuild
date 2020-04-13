package com.azar.fsdemo.statemachine.lambda.input;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PersistBatchJobDetailsHandler implements RequestHandler<Object, BatchJobDetails> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PersistBatchJobDetailsHandler.class.getName());

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	private final DynamoDB dynamoDB = new DynamoDB(client);

	@Override
	public BatchJobDetails handleRequest(Object input, Context context) {
		Map inputMap = (LinkedHashMap) input;

		BatchJobDetails batchJobDetails = new BatchJobDetails();
		batchJobDetails.setJobDefinitionName(inputMap.get("jobDefinitionName").toString());
		batchJobDetails.setJobName(inputMap.get("jobName").toString());
		batchJobDetails.setTotalRecordsCount(Integer.parseInt(inputMap.get("totalRecordsCount").toString()));
		batchJobDetails.setChunkSize(Integer.parseInt(inputMap.get("chunkSize").toString()));
		batchJobDetails.setNumOfChunks(Integer.parseInt(inputMap.get("numOfChunks").toString()));

		LOGGER.info("Input: {}", batchJobDetails);

		createJobDetails(batchJobDetails);

		return batchJobDetails;
	}

	private void createJobDetails(BatchJobDetails details) {
		Table batchJobDetailsTable = dynamoDB.getTable("BatchJobDetails");
		Table chunkDetailsTable = dynamoDB.getTable("ChunkDetails");

		try {
			String jobId = "MJ-" + sdf.format(new Date());

			LOGGER.info("Creating item for BatchJobDetails table with the ID - {}", jobId);

			Item jobDetailsItem = new Item().withPrimaryKey("Id", jobId).withString("JobName", details.getJobName())
					.withString("JobDefinitionName", details.getJobDefinitionName())
					.withString("ExecutionDate", new Date().toString())
					.withInt("totalRecordsCount", details.getTotalRecordsCount())
					.withInt("chunkSize", details.getChunkSize()).withInt("numOfChunks", details.getNumOfChunks())
					.withString("BatchJobStatus", "READY");

			batchJobDetailsTable.putItem(jobDetailsItem);

			LOGGER.info("BatchJobDetail is persisted for the ID - {}", jobId);

			// iterate through chunk details
			for (int i = 0; i < details.getNumOfChunks(); i++) {
				String chunkId = "MJ-CHUNK-" + sdf.format(new Date()) + "-" + i;

				Item chunkItem = new Item().withPrimaryKey("Id", chunkId).withInt("ChunkIdx", i).withString("ChunkJobStatus",
						"READY");

				chunkDetailsTable.putItem(chunkItem);

				LOGGER.info("ChunkDetail is persisted with the ID - {}", chunkId);
			}
		} catch (Exception e) {
			LOGGER.error("Create BatchJobDetails failed. - {}", e.getMessage());
		}
	}

}
