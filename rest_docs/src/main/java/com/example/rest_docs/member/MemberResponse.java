package com.example.rest_docs.member;

import lombok.Getter;

@Getter
public class MemberResponse {

	private final String email;
	private final String name;

	public MemberResponse(final Member member) {
		this.email = member.getEmail();
		this.name = member.getName();
	}
}
