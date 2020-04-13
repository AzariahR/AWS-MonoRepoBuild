package com.azar.fsdemo.batchdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class AWSBatchDataProviderApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(AWSBatchDataProviderApplication.class.getName());

	public static void main(String... args) {
		SpringApplication.run(AWSBatchDataProviderApplication.class, args);

		LOGGER.info("BatchJobDataProvider App started successfully");
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AWSBatchDataProviderApplication.class);
	}
}
