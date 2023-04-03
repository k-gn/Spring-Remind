package com.example.rest_docs.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class MemberModificationRequest {

	@NotNull
	private String name;
}
