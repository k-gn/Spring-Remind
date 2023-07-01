package com.app.presentation.test.exception.controller.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class BindExceptionRequest {

	@NotBlank(message = "해당 값은 필수값입니다.")
	private final String value;

	@Max(value = 10, message = "최대 입력값은 10 입니다.")
	private final int number;

	public BindExceptionRequest(
		String value,
		int number
	) {
		this.value = value;
		this.number = number;
	}
}
