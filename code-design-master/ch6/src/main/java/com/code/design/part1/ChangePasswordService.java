package com.code.design.part1;

// 다양한 인증수단이나 다른 방법으로 비밀번호를 변경할 수 있는 대체성이 있다.
// 명확한 책임이란게 메서드가 1개만 있다는 건 아니다.
public interface ChangePasswordService {
    public void change(Long id, PasswordChangeRequest dto);
}
