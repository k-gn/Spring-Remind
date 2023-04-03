package com.example.rest_docs.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {

	@Email
	private String email;

	@NotNull
	private String name;

	public Member toEntity() {
		return new Member(email, name);
	}

	public static MemberSignUpRequest fixture() {
		return new MemberSignUpRequest("test@gmail.com", "test");
	}
}
