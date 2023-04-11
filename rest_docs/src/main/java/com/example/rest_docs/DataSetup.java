package com.example.rest_docs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.rest_docs.member.Member;
import com.example.rest_docs.member.MemberRepository;
import com.example.rest_docs.member.MemberStatus;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSetup implements ApplicationRunner {

	private final MemberRepository memberRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		final List<Member> members = new ArrayList<>();

		members.add(new Member("aaa@gmail.com", "aaa", MemberStatus.BAN));
		members.add(new Member("bbb@gmail.com", "bbb", MemberStatus.NORMAL));
		members.add(new Member("ccc@gmail.com", "ccc", MemberStatus.NORMAL));
		members.add(new Member("ddd@gmail.com", "ddd", MemberStatus.LOCK));

		memberRepository.saveAll(members);
	}
}
