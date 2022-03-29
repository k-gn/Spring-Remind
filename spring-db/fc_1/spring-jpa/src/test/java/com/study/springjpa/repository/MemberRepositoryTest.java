package com.study.springjpa.repository;

import com.study.springjpa.domain.Member;
import com.study.springjpa.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void before() {
        Member member = Member.builder()
                .name("hong")
                .build();
        Team team = Team.builder()
                .memberList(List.of(member))
                .build();


        memberRepository.save(member);
        teamRepository.save(team);
    }

    @Test
    void test() {

//        memberRepository.findAll().forEach(System.out::println);
//        Member member = memberRepository.findById(1L).orElse(null);
//        System.out.println(member);

        Team team = teamRepository.findById(1L).get();
        System.out.println(team);
    }
}