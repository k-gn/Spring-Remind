package com.app.domain.member.constants;

import java.util.Arrays;

public enum MemberType {

    KAKAO;

    public static boolean isMemberType(String type) {
        return Arrays.stream(MemberType.values())
                .anyMatch(memberType -> memberType.name().equals(type.toUpperCase()));
    }

}
