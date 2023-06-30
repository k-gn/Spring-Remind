package com.app.external.oauth.kakao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.app.domain.member.constant.MemberType;
import com.app.external.oauth.kakao.client.KakaoUserInfoClient;
import com.app.external.oauth.kakao.dto.KakaoUserInfoResponse;
import com.app.external.oauth.dto.OAuthAttributes;
import com.app.external.oauth.service.SocialLoginApiService;
import com.app.global.jwt.constant.GrantType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoSocialLoginApiService implements SocialLoginApiService {

	private final KakaoUserInfoClient kakaoUserInfoClient;

	@Override
	public OAuthAttributes getUserInfo(String accessToken) {
		String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf8";
		KakaoUserInfoResponse kakaoUserInfoResponse = kakaoUserInfoClient.getKakaoUserInfo(CONTENT_TYPE,
			GrantType.BEARER.getType() + " " + accessToken);
		KakaoUserInfoResponse.KakaoAccount kakaoAccount = kakaoUserInfoResponse.getKakaoAccount();
		String email = kakaoAccount.getEmail();

		return OAuthAttributes.builder()
			.email(!StringUtils.hasText(email) ? kakaoUserInfoResponse.getId() : email)
			.name(kakaoAccount.getProfile().getNickname())
			.profile(kakaoAccount.getProfile().getThumbnailImageUrl())
			.memberType(MemberType.KAKAO)
			.build();
	}

}
