package com.azar.secondapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.azar.secondapp.service.SecondAppRequestHandler;

@RestController
@RequestMapping(value = "secondApp")
public class SecondController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SecondAppRequestHandler requestHandler;

	@RequestMapping(value = "healthCheck", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> healthCheck() throws Exception {
		logger.info("Second App - healthCheck - GET method invoked");

		final String HEALTHY = "{\"status\":\"HEALTHY\"}";

		return new ResponseEntity<String>(HEALTHY, HttpStatus.OK);
	}
	
	@RequestMapping(value = "printSystemDetails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> printSystemDetails() throws Exception {
		logger.info("JAVA : {}", System.getProperty("java.version"));
		logger.info("OS : {}", System.getProperty("os.version"));
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "testHttpTrace", method = RequestMethod.GET)
	public ResponseEntity<String> testHttpTrace() throws Exception {
		logger.info("Second App - Test Http Trace - GET method - Request reached from FIRST App");

		String respMsg = "You are reading this message which is sent from Second App";
		return new ResponseEntity<String>(respMsg, HttpStatus.OK);
	}

	@RequestMapping(value = "testMultiThreadTrace", method = RequestMethod.GET)
	public ResponseEntity<String> testMultiThreadTrace() throws Exception {
		logger.info("Second App - Test MultiThread - GET method - Request reached from FIRST App");
		requestHandler.invokeMultiThreadTask();
		logger.info("Second App - Test MultiThread - GET method - Request COMPLETED for FIRST App");

		String respMsg = "MultiThread Task invoked & completed successfully in Second App.";
		return new ResponseEntity<String>(respMsg, HttpStatus.OK);
	}

	@RequestMapping(value = "testRandomErrorTrace", method = RequestMethod.GET)
	public ResponseEntity<String> testRandomErrorTrace() throws Exception {
		logger.info("Second App - Test RandomError - GET method - Request reached from FIRST App");

		String resp = requestHandler.handleRandomErrorRequest();

		return new ResponseEntity<String>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "tearDown", method = RequestMethod.GET)
	public ResponseEntity<String> tearDown() throws Exception {
		logger.info("Second App - TearDown - this instance ");

		requestHandler.teardown("teardown this instance");

		return new ResponseEntity<String>("Invoked Teaardown - This line wont execute", HttpStatus.OK);
	}

}
