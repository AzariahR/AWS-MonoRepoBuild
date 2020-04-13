package com.azar.firstapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.azar.firstapp.service.FirstAppRequestHandler;

@RestController
@RequestMapping(value = "firstApp")
public class FirstController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FirstAppRequestHandler requestHandler;

	@RequestMapping(value = "healthCheck", method = RequestMethod.GET)
	public ResponseEntity<String> healthCheck() throws Exception {
		logger.info("FIrst App - healthCheck - GET method invoked");

		final String HEALTHY = "{\"status\":\"HEALTHY\"}";

		return new ResponseEntity<String>(HEALTHY, HttpStatus.OK);
	}

	@RequestMapping(value = "checkHttpTrace", method = RequestMethod.GET)
	public ResponseEntity<String> checkTestHttpTrace() throws Exception {
		logger.info("++++++ First App - Invoking GET method - \r\n Test Http Trace");
		String resp = requestHandler.invokeHttpAPI();		
		logger.info("++++++ First App - Test Http Trace - GET method Completed with the response :{}", resp);

		return new ResponseEntity<String>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "checkMultiThreadTrace", method = RequestMethod.GET)
	public ResponseEntity<String> getTestMultiThreadTrace() throws Exception {
		logger.info("++++++ First App - Test Multi Thread - GET method invoked");
		String resp = requestHandler.invokeMultiThreadHttpAPI();
		logger.info("++++++ First App - Test MultiThread Trace - GET method Completed with the response - {}", resp);

		return new ResponseEntity<String>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "checkRandomErrorTrace", method = RequestMethod.GET)
	public ResponseEntity<String> checkRandomErrorTrace() throws Exception {
		logger.info("++++++ First App - Invoking GET method - Test Random Error Trace");
		String resp = requestHandler.invokeHttpAPIForRandomErrorTest();
		logger.info("++++++ First App - Test Random Error Trace - GET method Completed with the response - {}", resp);

		return new ResponseEntity<String>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "checkLocalHttpTrace", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void checkLocalTestHttpTrace() throws Exception {
		logger.info("++++++ First App - Invoking GET method - Test Http Trace");
		requestHandler.invokeHttpAPIForLocal();
		logger.info("++++++ First App - Test Http Trace - GET method Completed with the response");
	}
}
