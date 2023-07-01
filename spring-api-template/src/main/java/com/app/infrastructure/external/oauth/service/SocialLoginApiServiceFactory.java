package com.app.infrastructure.external.oauth.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.domain.member.constants.MemberType;

@Service
public class SocialLoginApiServiceFactory {

	// map 으로 동일 타입 받기
	private static Map<String, SocialLoginApiService> socialLoginApiServices;

	public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
		SocialLoginApiServiceFactory.socialLoginApiServices = socialLoginApiServices;
	}

	public static SocialLoginApiService getSocialLoginApiService(MemberType memberType) {
		String socialLoginApiServiceBeanName = "";

		if (MemberType.KAKAO.equals(memberType)) {
			socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
		}
		return socialLoginApiServices.get(socialLoginApiServiceBeanName);
	}

}
