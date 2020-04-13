package com.azar.secondapp;

import java.net.URL;
import java.util.concurrent.Executor;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.metrics.MetricsSegmentListener;
import com.amazonaws.xray.plugins.ElasticBeanstalkPlugin;
import com.amazonaws.xray.slf4j.SLF4JSegmentListener;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;

@Configuration
public class WebConfig {

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Bean
	public Filter tracingFilter() {
		return new AWSXRayServletFilter("Mono Second App");
	}

	static {
		AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withSegmentListener(new SLF4JSegmentListener()).withSegmentListener(new MetricsSegmentListener())
				.withPlugin(new ElasticBeanstalkPlugin());

		URL ruleFile = WebConfig.class.getResource("/sampling-rules.json");
		builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));

		AWSXRay.setGlobalRecorder(builder.build());

		// AWSXRay.beginSegment("SecondApp-DUMMY-SEG-init.");
		logger.info("++++ Initial Log of Second App.");

		// AWSXRay.endSegment();
	}

	@Bean(name = "azarAsyncTaskExecutor") 
	public Executor myThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(10);
		executor.setThreadNamePrefix("azarThreadPool-");
		executor.initialize();

		return executor;
	}
}
