package com.app.api.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.api.login.dto.OauthLogin;
import com.app.api.login.service.OauthLoginService;
import com.app.api.login.validator.OauthValidator;
import com.app.domain.member.constant.MemberType;
import com.app.global.util.AuthorizationHeaderUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OauthLoginController {

	private final OauthValidator oauthValidator;
	private final OauthLoginService oauthLoginService;

	@PostMapping("/login")
	public ResponseEntity<OauthLogin.Response> oauthLogin(
		@RequestBody OauthLogin.Request request,
		HttpServletRequest httpServletRequest
	) {
		String authorizationHeader = httpServletRequest.getHeader("Authorization");
		AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);
		oauthValidator.validateMemberType(request.getMemberType());

		String accessToken = authorizationHeader.split(" ")[1];
		OauthLogin.Response jwtTokenResponse = oauthLoginService
			.oauthLogin(accessToken, MemberType.from(request.getMemberType()));
		return ResponseEntity.ok(jwtTokenResponse);
	}

}
