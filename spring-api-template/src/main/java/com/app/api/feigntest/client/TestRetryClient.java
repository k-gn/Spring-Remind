package com.app.api.feigntest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.api.health.controller.response.HealthCheckResponse;

@FeignClient(url = "http://localhost:8081", name = "testRetryClient")
public interface TestRetryClient {

	@GetMapping(value = "/api/health-retry", consumes = "application/json")
	HealthCheckResponse healthCheckRetryTest();
}
