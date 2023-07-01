package com.app.presentation.health.controller.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HealthCheckResponse {

	private final String health;
	private final List<String> activeProfiles;

	@Builder
	private HealthCheckResponse(
		String health,
		List<String> activeProfiles
	) {
		this.health = health;
		this.activeProfiles = activeProfiles;
	}

	public static HealthCheckResponse of(
		String health,
		List<String> activeProfiles
	) {
		return HealthCheckResponse.builder()
			.health(health)
			.activeProfiles(activeProfiles)
			.build();
	}
}
