package com.app.infrastructure.external.oauth.service;

import com.app.infrastructure.external.oauth.dto.OAuthAttributes;

public interface SocialLoginApiService {

    OAuthAttributes getUserInfo(String accessToken);

}
