package com.app.api.health.controller;

import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.api.health.controller.response.HealthCheckResponse;

import lombok.RequiredArgsConstructor;

/*
	# 서버 상태 체크 API
	- 애플리케이션이 정상적으로 동작하고 있는지 확인하기 위한 API
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthCheckController {

	private final Environment environment;

	@GetMapping("/health")
	public ResponseEntity<HealthCheckResponse> healthCheck() {
		return ResponseEntity.ok(HealthCheckResponse.of("ok", Arrays.asList(environment.getActiveProfiles())));
	}
}
