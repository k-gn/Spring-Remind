package com.code.design;

import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) // 필수값이 비어있는 객체 생성을 예방하기 위함
public class SignUpRequest {

    @NotEmpty
    private String name;

    // 테스트 코드에서 밖에 사용하지 않는 코드...
//    public SignUpRequest(String name) {
//        this.name = name;
//    }

    public Member toEntity() {
        return new Member(name);
    }
}