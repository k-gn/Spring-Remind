package com.code.design.test_4;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import com.code.design.Member;
import com.code.design.MemberFindService;
import com.code.design.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class MemberFindServiceMockTestSupport extends MockTestSupport {

    // 제어할 수 없는, 제어해선 안되는 외부 인프라인 경우에 Mocking
    // 그 외에는 굳이?
    @InjectMocks
    private MemberFindService memberFindService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void mock_test() {
        //given
        final Member member = new Member("yun");

        given(memberRepository.findByName("yun")).willReturn(member);

        //when
        final Member findMember = memberFindService.findByName("yun");

        //then
        then(findMember.getName()).isEqualTo("yun");
    }
}