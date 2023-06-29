package com.app.api.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.api.member.dto.MemberInfoResponse;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;

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
