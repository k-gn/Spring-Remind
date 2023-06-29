package com.app.domain.member.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberType {

    KAKAO;

    public static MemberType from(String type) {
        return MemberType.valueOf(type.toUpperCase());
    }

    public static boolean isMemberType(String type) {
        return Arrays.stream(MemberType.values())
                .anyMatch(memberType -> memberType.name().equals(type.toUpperCase()));
    }

}
