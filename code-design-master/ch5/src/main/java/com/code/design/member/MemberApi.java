package com.code.design.member;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberRepository memberRepository;

    /*
        - 컨트롤러는 요청값을 검증할 책임을 가지고 있다.
     */
    @PostMapping
    public Member create(@RequestBody @Valid final SignUpRequest dto) {
        return memberRepository.save(Member.builder()
            .email(dto.getEmail())
            .build());
    }
}
