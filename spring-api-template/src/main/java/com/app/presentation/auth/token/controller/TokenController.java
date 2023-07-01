package com.app.presentation.auth.token.controller;

import com.app.presentation.auth.token.controller.response.AccessTokenResponse;
import com.app.application.auth.service.TokenService;
import com.app.global.util.AuthorizationHeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final TokenService tokenService;

    // access-token 재발급 요청
    @PostMapping("/access-token/issue")
    public ResponseEntity<AccessTokenResponse> createAccessToken(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

        String refreshToken = authorizationHeader.split(" ")[1];
        AccessTokenResponse accessTokenResponse = tokenService.createAccessTokenByRefreshToken(refreshToken);
        return ResponseEntity.ok(accessTokenResponse);
    }
}
