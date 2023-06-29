package com.app.api.feigntest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.api.health.controller.response.HealthCheckResponse;

@FeignClient(url = "http://localhost:8080", name = "testClient")
public interface TestClient {

	@GetMapping(value = "/api/health", consumes = "application/json")
	HealthCheckResponse healthCheckTest();

	@GetMapping(value = "/api/health-fail", consumes = "application/json")
	HealthCheckResponse healthCheckFailTest();
}
