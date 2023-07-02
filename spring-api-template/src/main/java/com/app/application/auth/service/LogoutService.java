package com.app.application.auth.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.domain.member.Member;
import com.app.application.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.TokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutService {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	public void logout(String accessToken) {
		tokenProvider.validateToken(accessToken);

		Claims tokenClaims = tokenProvider.getTokenClaims(accessToken);
		String tokenType = tokenClaims.getSubject();
		if (!TokenType.isAccessToken(tokenType)) {
			throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
		}

		Long memberId = Long.valueOf((Integer)tokenClaims.get("memberId"));
		Member member = memberService.findMemberByMemberId(memberId);
		member.expireRefreshToken(LocalDateTime.now());
	}

}
