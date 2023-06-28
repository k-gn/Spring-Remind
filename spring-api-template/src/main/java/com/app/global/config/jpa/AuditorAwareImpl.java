package com.app.global.config.jpa;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

	private final HttpServletRequest httpServletRequest;

	@Override
	public Optional<String> getCurrentAuditor() {
		String modifiedBy = httpServletRequest.getRequestURI();
		if (!StringUtils.hasText(modifiedBy)) {
			modifiedBy = "unknown";
		}
		return Optional.of(modifiedBy);
	}
}
