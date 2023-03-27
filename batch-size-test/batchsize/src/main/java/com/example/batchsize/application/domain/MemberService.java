package com.example.batchsize.application.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.batchsize.domain.pk_identity_strategy.Company;
import com.example.batchsize.domain.pk_identity_strategy.Person;
import com.example.batchsize.domain.pk_identity_strategy.PersonRepository;
import com.example.batchsize.domain.pk_none_strategy.Member;
import com.example.batchsize.domain.pk_none_strategy.MemberRepository;
import com.example.batchsize.domain.pk_none_strategy.Team;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public void save(
		Member member,
		Team team
	) {
		member.addTeam(team);
		memberRepository.save(member);
	}
}
