package com.app.presentation.web.kakaotoken.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.presentation.web.kakaotoken.client.KakaoTokenClient;
import com.app.presentation.web.kakaotoken.dto.KakaoToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoTokenProvider {

	private final KakaoTokenClient kakaoTokenClient;

	@Value("${kakao.client.id}")
	private String clientId;

	@Value("${kakao.client.secret}")
	private String clientSecret;

	public KakaoToken.Response getAccessTokenByCode(String code) {
		String contentType = "application/x-www-form-urlencoded;charset=utf-8";
		KakaoToken.Request request = KakaoToken.Request.of(
			clientId,
			clientSecret,
			code,
			"http://localhost:8080/oauth/kakao/callback"
		);
		return kakaoTokenClient.requestToken(contentType, request);
	}
}
