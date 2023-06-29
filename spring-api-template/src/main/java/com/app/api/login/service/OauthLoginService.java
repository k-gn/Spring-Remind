package com.app.api.login.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.api.login.dto.OauthLogin;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.external.oauth.model.OAuthAttributes;
import com.app.external.oauth.service.SocialLoginApiService;
import com.app.external.oauth.service.SocialLoginApiServiceFactory;
import com.app.global.jwt.dto.JwtToken;
import com.app.global.jwt.service.TokenManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

	private final MemberService memberService;
	private final TokenManager tokenManager;

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
		JwtToken jwtToken = tokenManager.createJwtToken(oauthMember.getMemberId(), oauthMember.getRole());
		oauthMember.updateRefreshToken(jwtToken);
		return OauthLogin.Response.of(jwtToken);
	}

}
