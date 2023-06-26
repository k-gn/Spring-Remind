package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final UserStatus status;
    private final Long lastLoginAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .status(user.getStatus())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }
}
