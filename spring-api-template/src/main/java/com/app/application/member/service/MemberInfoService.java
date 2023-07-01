package com.app.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.presentation.member.controller.response.MemberInfoResponse;
import com.app.domain.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

	private final MemberService memberService;

	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfo(Long memberId) {
		Member member = memberService.findMemberByMemberId(memberId);
		return MemberInfoResponse.of(member);
	}

}
