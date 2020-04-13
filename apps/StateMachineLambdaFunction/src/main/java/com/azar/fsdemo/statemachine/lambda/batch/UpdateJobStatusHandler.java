package com.azar.fsdemo.statemachine.lambda.batch;

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
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateJobStatusHandler implements RequestHandler<Object, String> {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateJobStatusHandler.class.getName());

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	private final DynamoDB dynamoDB = new DynamoDB(client);

	@Override
	public String handleRequest(Object input, Context context) {
		String batchJobId = "MJ-" + sdf.format(new Date());

		String status = "COMPLETED";

		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", batchJobId)
				.withReturnValues(ReturnValue.UPDATED_OLD).withUpdateExpression("set #jsts = :nStatus")
				.withNameMap(new NameMap().with("#jsts", "BatchJobStatus"))
				.withValueMap(new ValueMap().withString(":nStatus", status));

		try {
			Table batchJobDetailsTable = dynamoDB.getTable("BatchJobDetails");
			UpdateItemOutcome outcome = batchJobDetailsTable.updateItem(updateItemSpec);
			LOGGER.info("Updated Status to COMPLETED successfully - {}", outcome);
		} catch (Exception e) {
			LOGGER.error("Error while updating the BatchJob Status to COMPLETED - {}", e);
			status = "FAILED";
		}

		return status;
	}
}
