package com.azar.firstapp.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class FirstAppUtil {

	private FirstAppUtil() {
	}

	public static CloseableHttpClient getXrayHttpClient() {
		return com.amazonaws.xray.proxies.apache.http.HttpClientBuilder.create().build();
	}

	public static CloseableHttpClient getNormalHttpClient() {
		return HttpClientBuilder.create().build();
	}
}
