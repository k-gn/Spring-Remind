package com.app.application.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.presentation.auth.login.dto.OauthLogin;
import com.app.domain.member.constants.MemberType;
import com.app.domain.member.constants.Role;
import com.app.domain.member.Member;
import com.app.application.member.service.MemberService;
import com.app.infrastructure.external.oauth.dto.OAuthAttributes;
import com.app.infrastructure.external.oauth.service.SocialLoginApiService;
import com.app.infrastructure.external.oauth.service.SocialLoginApiServiceFactory;
import com.app.global.jwt.dto.JwtToken;
import com.app.global.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	public OauthLogin.Response oauthLogin(
		String accessToken,
		MemberType memberType
	) {
		SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
		OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
		log.info("userInfo : {}", userInfo);

		Optional<Member> optionalMember = memberService.findMemberByEmail(userInfo.getEmail());
		Member oauthMember;
		if (optionalMember.isEmpty()) { // 신규 회원 가입
			oauthMember = userInfo.toMemberEntity(memberType, Role.ADMIN);
			oauthMember = memberService.registerMember(oauthMember);
		} else { // 기존 회원일 경우
			oauthMember = optionalMember.get();
		}
		// 토큰 생성
		JwtToken jwtToken = tokenProvider.createJwtToken(oauthMember.getMemberId(), oauthMember.getRole());
		oauthMember.updateRefreshToken(jwtToken);
		return OauthLogin.Response.of(jwtToken);
	}

}
