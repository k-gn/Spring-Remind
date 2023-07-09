package com.app.application.auth.service;

import com.app.presentation.auth.token.controller.response.AccessTokenResponse;
import com.app.domain.member.Member;
import com.app.application.member.service.MemberService;
import com.app.global.jwt.constant.GrantType;
import com.app.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @Transactional
    public AccessTokenResponse createAccessTokenByRefreshToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        Member member = memberService.findMemberByRefreshToken(refreshToken);

        Date accessTokenExpireTime = tokenProvider.createAccessTokenExpireTime();
        String accessToken = tokenProvider.createAccessToken(member.getMemberId(), member.getRole(), accessTokenExpireTime);

        return AccessTokenResponse.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .build();
    }
}
