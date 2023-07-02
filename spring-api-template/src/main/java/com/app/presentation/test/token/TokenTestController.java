package com.app.presentation.test.token;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.domain.member.constants.Role;
import com.app.global.jwt.dto.JwtToken;
import com.app.global.jwt.TokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/token-test")
@RequiredArgsConstructor
public class TokenTestController {

	private final TokenProvider tokenProvider;

	@GetMapping("/create")
	public JwtToken createJwtToken() {
		return tokenProvider.createJwtToken(1L, Role.ADMIN);
	}

	@GetMapping("/valid")
	public String validateJwtToken(@RequestParam String token) {
		tokenProvider.validateToken(token);
		Claims tokenClaims = tokenProvider.getTokenClaims(token);
		log.info("memberId : {}", tokenClaims.get("memberId"));
		log.info("role : {}", tokenClaims.get("role"));
		return "success";
	}

}
