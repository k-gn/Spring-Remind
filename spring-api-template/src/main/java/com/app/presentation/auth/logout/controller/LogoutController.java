package com.app.presentation.auth.logout.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.application.auth.service.LogoutService;
import com.app.global.util.AuthorizationHeaderUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogoutController {

	private final LogoutService logoutService;

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
		String authorizationHeader = httpServletRequest.getHeader("Authorization");
		AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

		String accessToken = authorizationHeader.split(" ")[1];
		logoutService.logout(accessToken);

		return ResponseEntity.ok("logout success");
	}

}
