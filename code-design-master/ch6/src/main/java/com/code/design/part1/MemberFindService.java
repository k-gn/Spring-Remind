package com.code.design.part1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 조회 책임에 따른 분리
// 서비스의 크기와 책임을 어떻게 명확하게? => 서비스 클래스를 행위 중심으로 만드는 걸 권장
// 대체성이 없다면 굳이 인터페이스로 만들 필요가 없다.
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFindService {

    private final MemberRepository memberRepository;

    public Member findById(final Long id) {
        final Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("id: " + id + " not found"));
        return member;
    }

    public Member findByEmail(final String email) {
        final Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("email: " + email + " not found");
        }
        return member;
    }
}
