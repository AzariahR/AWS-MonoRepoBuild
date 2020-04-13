package com.azar.firstapp.util;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class DWHHealthIndicator  implements HealthIndicator {
    private final String message_key = "Service B";
    @Override
    public Health health() {
        if (!isRunningServiceB()) {
            return Health.down().withDetail(message_key, "Not Available").build();
        }
        return Health.up().withDetail(message_key, "Available").build();
    }
    private Boolean isRunningServiceB() {
        Boolean isRunning = false;
        // Logic Skipped
        return isRunning;
    }


}
