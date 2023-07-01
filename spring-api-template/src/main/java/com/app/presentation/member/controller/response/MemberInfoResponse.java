package com.app.presentation.member.controller.response;

import com.app.domain.member.constants.Role;
import com.app.domain.member.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponse {

	private final Long memberId;

	private final String email;

	private final String memberName;

	private final String profile;

	private final Role role;

	public static MemberInfoResponse of(Member member) {
		return MemberInfoResponse.builder()
			.memberId(member.getMemberId())
			.memberName(member.getMemberName())
			.email(member.getEmail())
			.profile(member.getProfile())
			.role(member.getRole())
			.build();
	}
}
