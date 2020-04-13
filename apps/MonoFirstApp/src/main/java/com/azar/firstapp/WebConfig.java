package com.azar.firstapp;

import java.net.URL;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	/*@Value("${AWS_ACCESS_KEY_ID:ACCESSID}")
	private String awsAccessKeyId;

	@Value("${AWS_SECRET_ACCESS_KEY:SecREtKey}")
	private String awsSecretAccessKey;

	@Value("${AWS_REGION}")
	private String awsRegion;*/

	@Bean
	public Filter tracingFilter() {
		return new AWSXRayServletFilter("Mono First App");
	}

	/*@PostConstruct
	private void displayEnvVariables() {
		logger.info("ENV VARIABL -> E" + awsAccessKeyId + " " + awsSecretAccessKey + " " + awsRegion);
	} 

	@Bean
	public AWSStaticCredentialsProvider awsStaticCredentialsProvider() {
		logger.info("ENV VARIABL -> E" + awsAccessKeyId + " " + awsSecretAccessKey + " " + awsRegion);
		return new AWSStaticCredentialsProvider(new BasicAWSCredentials("ACCESSID", "SecREtKey"));
	}

	@Bean
	public AWSSimpleSystemsManagement ssmClient() {
		logger.info("ENV VARIABL -> E" + awsAccessKeyId + " " + awsSecretAccessKey + " " + awsRegion);
		AWSCredentialsProvider credentials = InstanceProfileCredentialsProvider.getInstance();
		return AWSSimpleSystemsManagementClientBuilder.standard().withRegion("ap-south-1").withCredentials(credentials).build();
	}

	@Bean
	public AWSSecretsManager smClient() {
		logger.info("ENV VARIABL -> E" + awsAccessKeyId + " " + awsSecretAccessKey + " " + awsRegion);
		return AWSSecretsManagerClientBuilder.standard().withRegion("ap-south-1").withCredentials(awsStaticCredentialsProvider()).build();
	}*/

	/**
	 * @Bean public SecurityWebFilterChain
	 *       securityWebFilterChain(ServerHttpSecurity http) { return
	 *       http.authorizeExchange().pathMatchers("/actuator/**").permitAll().anyExchange().authenticated().and().build();
	 *       }
	 */

	static {
		AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withSegmentListener(new SLF4JSegmentListener()).withSegmentListener(new MetricsSegmentListener())
				.withPlugin(new ElasticBeanstalkPlugin());

		URL ruleFile = WebConfig.class.getResource("/sampling-rules.json");
		builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));

		AWSXRay.setGlobalRecorder(builder.build());
 
		//AWSXRay.beginSegment("FirstApp-DUMMY-SEG-init.");

		logger.info("WebConfig initialization...");
		logger.info("++++Initial log First App");

		//AWSXRay.endSegment();
	}
}
