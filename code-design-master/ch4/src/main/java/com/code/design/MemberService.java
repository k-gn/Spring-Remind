package com.code.design;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member createUncheckedException() {
        final Member member = memberRepository.save(new Member("yun"));
        if (true) {
            throw new RuntimeException();
        }
        return member;
    }

    // Checked Exception는 반드시 예외처리 / 트랜잭션 롤백 안됨
    // 예외를 계속 던지기 보단 감싼 후 런타임 예외로 처리해주는게 좋다.
    public Member createCheckedException() throws IOException {
        final Member member = memberRepository.save(new Member("wan"));
        if (true) {
            throw new IOException();
        }
        return member;
    }

    public Member findById(long id) {
        return memberRepository.findById(id).get();
    }

}
