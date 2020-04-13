package com.azar.fsdemo.awsbatchjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import com.azar.fsdemo.awsbatchjob.service.BatchJobExecutor;

@SpringBootApplication
@ComponentScan
public class AWSBatchJobApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(AWSBatchJobApplication.class.getName());

	public static void main(String... args) {
		SpringApplication.run(AWSBatchJobApplication.class, args);

		LOGGER.info("BatchJob started successfully");

		new BatchJobExecutor().startBatchJob();
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AWSBatchJobApplication.class);
	}
}
