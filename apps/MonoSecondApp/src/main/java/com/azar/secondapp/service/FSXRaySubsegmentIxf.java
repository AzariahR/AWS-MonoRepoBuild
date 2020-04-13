package com.azar.secondapp.service;

import org.slf4j.MDC;

import org.springframework.util.StringUtils;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.entities.Entity;

public interface FSXRaySubsegmentIxf {
	public final AWSXRayRecorder recorder = AWSXRay.getGlobalRecorder();

	public default void initSubsegment(Entity parentSegment) {
		recorder.setTraceEntity(parentSegment);
		AWSXRay.beginSubsegment("## Asynchronously processing data in child thread");
		MDC.put("AWS-XRAY-TRACE-ID", "AWS-XRAY-TRACE-ID: " + parentSegment.getTraceId().toString());
	}

	public default void endSubsegment() {
		AWSXRay.endSubsegment();
	}

	public default void attachToSubsegment(Exception ex) {
		AWSXRay.getCurrentSubsegment().addException(ex);
	}

	public default void attachToSegment(Exception ex) {
		AWSXRay.getCurrentSegment().addException(ex);
	}

	public default void putSubsegmentAnnotation(String key, Object value) {
		if (!StringUtils.isEmpty(key) && value != null) {
			if (value instanceof String) {
				AWSXRay.getCurrentSubsegment().putAnnotation(key, (String) value);
			}
			else if (value instanceof Boolean) {
				AWSXRay.getCurrentSubsegment().putAnnotation(key, (Boolean) value);
			}
			else if (value instanceof Number) {
				AWSXRay.getCurrentSubsegment().putAnnotation(key, (Number) value);
			}
			else {
				AWSXRay.getCurrentSubsegment().putAnnotation(key, value.toString());
			}
		}
	}
}
