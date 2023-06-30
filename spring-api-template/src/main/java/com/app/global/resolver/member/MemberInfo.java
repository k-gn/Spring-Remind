package com.app.global.resolver.member;

import com.app.domain.member.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfo {

    private final Long memberId;
    private final Role role;

    @Builder
    public MemberInfo(
        Long memberId,
        Role role
    ) {
        this.memberId = memberId;
        this.role = role;
    }
}
