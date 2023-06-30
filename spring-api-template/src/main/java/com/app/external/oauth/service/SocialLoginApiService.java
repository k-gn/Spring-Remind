package com.app.external.oauth.service;

import com.app.external.oauth.dto.OAuthAttributes;

public interface SocialLoginApiService {

    OAuthAttributes getUserInfo(String accessToken);

}
