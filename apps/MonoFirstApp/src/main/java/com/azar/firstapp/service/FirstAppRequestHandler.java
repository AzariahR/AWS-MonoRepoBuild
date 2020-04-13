package com.azar.firstapp.service;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azar.firstapp.util.FirstAppUtil;

@Component
public class FirstAppRequestHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("http://${SECOND_APP_DNS_ENDPOINT}/secondApp/")
	private String secondAppServiceBaseUrl;

	public String invokeHttpAPI() {
		HttpGet httpGet = new HttpGet(secondAppServiceBaseUrl + "testHttpTrace");

		try (CloseableHttpResponse response = FirstAppUtil.getXrayHttpClient().execute(httpGet)) {
			String resp = EntityUtils.toString(response.getEntity());
			logger.info("Got Success response :) - {}", resp);
			return resp;
		}
		catch (IOException ioe) {
			logger.error("!!! Error while invoking Second App - Test HTTP - secondService.azar.demo:3767 - TA - {}", ioe);
			return ioe.getMessage();
		}
	}

	public String invokeMultiThreadHttpAPI() {
		HttpGet httpGet = new HttpGet(secondAppServiceBaseUrl + "testMultiThreadTrace");

		try (CloseableHttpResponse response = FirstAppUtil.getXrayHttpClient().execute(httpGet)) {
			return EntityUtils.toString(response.getEntity());
		}
		catch (IOException ioe) {
			logger.error("Error while invoking Second App - Test MultiThread - Http GET API from FIRST APP- {}", ioe);
			return ioe.toString();
		}
	}

	public String invokeHttpAPIForRandomErrorTest() {
		HttpGet httpGet = new HttpGet(secondAppServiceBaseUrl + "testRandomErrorTrace");

		try (CloseableHttpResponse response = FirstAppUtil.getXrayHttpClient().execute(httpGet)) {
			return EntityUtils.toString(response.getEntity());
		}
		catch (IOException ioe) {
			logger.error("Error while invoking Second App - Test RandomeErrror - Http GET API from FIRST APP- {}", ioe);
			return ioe.toString();
		}
	}

	public void invokeHttpAPIForLocal() {
		HttpGet httpGet = new HttpGet("http://localhost:3767/api/second/testHttpTrace");

		try (CloseableHttpResponse response = FirstAppUtil.getXrayHttpClient().execute(httpGet)) {
			logger.info("++++ HURRAY - SUCCESS - Invoking Second App - Test HTTP - localhost:3033 WORKS");
		}
		catch (IOException ioe) {
			logger.error("!!! Error while invoking Second App - Test HTTP - localhost - {}", ioe);
		}
	}

}
