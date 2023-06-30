package com.app.external.oauth.dto;

import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OAuthAttributes {

	private final String name;
	private final String email;
	private final String profile;
	private final MemberType memberType;

	@Builder
	public OAuthAttributes(
		String name,
		String email,
		String profile,
		MemberType memberType
	) {
		this.name = name;
		this.email = email;
		this.profile = profile;
		this.memberType = memberType;
	}

	public Member toMemberEntity(
		MemberType memberType,
		Role role
	) {
		return Member.builder()
			.memberName(name)
			.email(email)
			.memberType(memberType)
			.profile(profile)
			.role(role)
			.build();
	}

}
