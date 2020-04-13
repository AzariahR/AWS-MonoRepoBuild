package com.azar.secondapp.service;

import org.aspectj.lang.ProceedingJoinPoint;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.entities.Entity;
import com.amazonaws.xray.entities.Subsegment;

//@Aspect
//@Component
public class XRaySubSegmentCreator {
	// private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final AWSXRayRecorder recorder = AWSXRay.getGlobalRecorder();

	//@Around("execution(* com.azar.secondapp.service.ThreadTask.runInSeparateThread(..))")
	public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();

		Entity mainThreadEntity = (Entity) args[1];
		recorder.setTraceEntity(mainThreadEntity);

		Subsegment subsegment = AWSXRay.beginSubsegment("## Asynchronously processing data in child thread");

		try {
			return pjp.proceed();
		}
		catch (Exception ex) {
			subsegment.addException(ex);
			throw ex; // propagate the exception
		}
		finally {
			AWSXRay.endSubsegment();
		}
	}
}
