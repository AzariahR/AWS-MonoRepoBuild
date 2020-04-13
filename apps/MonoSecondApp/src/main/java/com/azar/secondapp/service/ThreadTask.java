package com.azar.secondapp.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.xray.entities.Entity;

@Component
public class ThreadTask implements FSXRaySubsegmentIxf {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Async
	public CompletableFuture<String> runInSeparateThread(Integer num, Entity parentSegment) {
		initSubsegment(parentSegment);

		logger.info("Started processing for request {} using thread {}", num, Thread.currentThread());

		CompletableFuture<String> result = null;
		try {
			int randomVal = (int) (Math.random() * 29 / Math.random() * 7);
			int roundedRandomVal = randomVal - (randomVal % 100);

			result = CompletableFuture.completedFuture(num + " " + true + " " + roundedRandomVal);

			putSubsegmentAnnotation("Thread" + num, roundedRandomVal);

			if (randomVal % 10 > 5) {
				throw new Exception("Throwing Random Exception - " + randomVal);
			}

			logger.info("Completed processing for request {} using thread {}", num, Thread.currentThread());
		}
		catch (Exception ex) {
			result = CompletableFuture.completedFuture(num + " " + false);
			logger.error("Exception occurred while processing for request {} using thread {}", num, Thread.currentThread());
			attachToSubsegment(ex);
		}
		finally {
			endSubsegment();
		}

		return result;
	}
}
