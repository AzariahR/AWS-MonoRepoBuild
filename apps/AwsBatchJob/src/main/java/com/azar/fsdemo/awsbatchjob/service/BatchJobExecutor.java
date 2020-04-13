package com.azar.fsdemo.awsbatchjob.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BatchJobExecutor {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private DynamoDBClient dbClient = new DynamoDBClient();

	public void startBatchJob() {
		int batchJobArrayIndex = Integer.parseInt(System.getenv("AWS_BATCH_JOB_ARRAY_INDEX"));
		LOGGER.info("Starting Batch Job with the index- {} ", batchJobArrayIndex);

		int attemptCount = Integer.parseInt(System.getenv("AWS_BATCH_JOB_ATTEMPT"));

		// stop the container abnormally to demo retry scenario
		/*
		 * int randomNum = new Random().nextInt(10); if (randomNum > 5) { LOGGER.
		 * info("Going to STOP this Child Batch Job abruptly for the RandomValue - {}",
		 * randomNum); System.exit(123); } else {
		 * LOGGER.info("Wont STOP this Child Batch Job since random value is - {}",
		 * randomNum); }
		 */

		if (batchJobArrayIndex == 1 && attemptCount < 3) {
			System.exit(123);
		}

		// update chunk Job Status to "IN PROGRESS"
		dbClient.updateChunk(batchJobArrayIndex, "IN-PROGRESS");

		LOGGER.info("Send request to get {} records for the BatchJob Index(Query Page Number) - {}", 1000,
				batchJobArrayIndex);

		LOGGER.info("Process the retrieved records in the Query Page {} Resultset", batchJobArrayIndex);

		LOGGER.info("Batch Job completed successfully & Send the notification/result to the target");

		// update chunk Job Status to "COMPLETED"
		dbClient.updateChunk(batchJobArrayIndex, "COMPLETED");
	}
}
