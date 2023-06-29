package com.app.web.kakaotoken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.web.kakaotoken.dto.KakaoToken;
import com.app.web.kakaotoken.service.KakaoTokenProvider;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

	private final KakaoTokenProvider kakaoTokenProvider;

	@GetMapping("/login")
	public String login() {
		return "loginForm";
	}

	@GetMapping("/oauth/kakao/callback")
	@ResponseBody
	public KakaoToken.Response login(String code) {
		return kakaoTokenProvider.getAccessTokenByCode(code);
	}
}
