package com.app.presentation.auth.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.application.auth.service.OauthLoginService;
import com.app.global.util.AuthorizationHeaderUtils;
import com.app.presentation.auth.login.dto.OauthLogin;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OauthLoginController {

	private final OauthLoginService oauthLoginService;

	@PostMapping("/login")
	public ResponseEntity<OauthLogin.Response> oauthLogin(
		@RequestBody OauthLogin.Request request,
		HttpServletRequest httpServletRequest
	) {
		String authorizationHeader = httpServletRequest.getHeader("Authorization");
		AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

		String accessToken = authorizationHeader.split(" ")[1];
		OauthLogin.Response jwtTokenResponse = oauthLoginService
			.oauthLogin(accessToken, request.getMemberType());
		return ResponseEntity.ok(jwtTokenResponse);
	}

}
