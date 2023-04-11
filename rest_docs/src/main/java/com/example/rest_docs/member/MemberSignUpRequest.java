package com.example.rest_docs.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {

	@Email
	@Size(max = 30)
	private String email;

	@NotNull
	@Size(max = 10)
	private String name;

	private MemberStatus status;

	public Member toEntity() {
		return new Member(email, name, status);
	}

}
