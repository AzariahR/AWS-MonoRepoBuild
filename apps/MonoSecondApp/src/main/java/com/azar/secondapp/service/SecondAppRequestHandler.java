package com.azar.secondapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.xray.AWSXRay;

@Component
public class SecondAppRequestHandler implements FSXRaySubsegmentIxf {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ThreadTask threadTask;

	public void invokeMultiThreadTask() {
		List<CompletableFuture<String>> futures = new ArrayList<>();

		logger.info("Invoking 3 parallel threads from MultiThreadTaskHandler");

		for (int i = 0; i < 3; i++) {
			futures.add(threadTask.runInSeparateThread(i, AWSXRay.getGlobalRecorder().getCurrentSegment()));
		}

		// process threads' results
		futures.forEach(result -> {
			try {
				logger.info("Result got from child Thread {}", result.get());
			}
			catch (Exception e) {
				logger.error("Error while getting result from child thread - {}", e);
			}
		});

		logger.info("All thread results are processed successfully ");
	}

	public String handleRandomErrorRequest() throws Exception {
		int randomVal = (int) (Math.random() * 17 / Math.random() * 5);

		if (randomVal % 10 > 5) {
			Exception ex = new Exception("Throwing Random Exception to check Error logs in XRAY traces - " + randomVal);

			attachToSegment(ex);
			throw ex;
		}
		else {
			return "Second App - Test RandomError - COMPLETED successfully with the Random Value" + randomVal;
		}
	}

	public void teardown(String t) {
		String m = "trweytwe";

		teardown(m);
	}
}
