package com.app.global.jwt.constant;

import lombok.Getter;

@Getter
public enum TokenType {

    ACCESS, REFRESH;

    public static boolean isAccessToken(String tokenType) {
        return TokenType.ACCESS.name().equals(tokenType);
    }

}
