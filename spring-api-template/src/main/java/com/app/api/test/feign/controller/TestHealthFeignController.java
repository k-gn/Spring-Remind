package com.app.api.test.feign.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.api.test.feign.client.TestClient;
import com.app.api.test.feign.client.TestRetryClient;
import com.app.api.health.controller.response.HealthCheckResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestHealthFeignController {

	private final TestClient testClient;
	private final TestRetryClient testRetryClient;

	@GetMapping("/health/feign-test")
	public ResponseEntity<HealthCheckResponse> healthCheckTest() {
		return ResponseEntity.ok(testClient.healthCheckTest());
	}

	@GetMapping("/health/feign-fail")
	public ResponseEntity<HealthCheckResponse> feignFail() {
		return ResponseEntity.ok(testClient.healthCheckFailTest());
	}

	@GetMapping("/health/feign-retry")
	public ResponseEntity<HealthCheckResponse> feignRetry() {
		return ResponseEntity.ok(testRetryClient.healthCheckRetryTest());
	}
}

