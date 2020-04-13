package com.azar.fsdemo.awsbatchjob.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

public class DynamoDBClient {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	private final DynamoDB dynamoDB = new DynamoDB(client);

	public void updateChunk(int batchIdx, String updateStatus) {
		String chunkId = "MJ-CHUNK-" + sdf.format(new Date()) + "-" + batchIdx;

		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", chunkId)
				.withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set #csts = :newStatus")
				.withNameMap(new NameMap().with("#csts", "ChunkJobStatus"))
				.withValueMap(new ValueMap().withString(":newStatus", updateStatus));

		try {
			Table chunkDetailsTable = dynamoDB.getTable("ChunkDetails");
			UpdateItemOutcome outcome = chunkDetailsTable.updateItem(updateItemSpec);
			LOGGER.info("Updated Status to {} successfully - {}", updateStatus, outcome);
		} catch (Exception e) {
			LOGGER.error("Error while updating the Chunk Status -{} - {}", updateStatus, e);
		}
	}
}
