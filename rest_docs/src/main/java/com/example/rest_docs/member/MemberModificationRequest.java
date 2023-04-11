package com.example.rest_docs.member;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberModificationRequest {

	@NotNull
	@Size(max = 10)
	private String name;

}
