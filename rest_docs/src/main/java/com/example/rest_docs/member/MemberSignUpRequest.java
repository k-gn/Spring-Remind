package com.example.rest_docs.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class MemberSignUpRequest {

	@Email
	private String email;

	@NotNull
	private String name;

	public Member toEntity() {
		return new Member(email, name);
	}
}
