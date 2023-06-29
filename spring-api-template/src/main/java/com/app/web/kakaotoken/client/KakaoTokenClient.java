package com.app.web.kakaotoken.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.app.web.kakaotoken.dto.KakaoToken;

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoTokenClient {

	@PostMapping(value = "/oauth/token", consumes = "application/json")
	KakaoToken.Response requestToken(
		@RequestHeader("Content-Type") String contentType,
		@SpringQueryMap KakaoToken.Request request
	);

}
