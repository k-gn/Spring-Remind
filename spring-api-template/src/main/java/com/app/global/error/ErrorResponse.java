package com.app.global.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

	private final String code;
	private final List<String> messages;

	@Builder
	private ErrorResponse(
		String code,
		List<String> messages
	) {
		this.code = code;
		this.messages = messages;
	}

	public static ErrorResponse of(
		String code,
		String message
	) {
		return ErrorResponse.builder()
			.code(code)
			.messages(List.of(message))
			.build();
	}

	public static ErrorResponse of(
		String code,
		BindingResult bindingResult
	) {
		return ErrorResponse.builder()
			.code(code)
			.messages(createMessages(bindingResult))
			.build();
	}

	private static List<String> createMessages(BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		return fieldErrors.stream()
			.map(ErrorResponse::createMessage)
			.collect(Collectors.toList());
	}

	private static String createMessage(FieldError fieldError) {
		return fieldError.getField() + " : " + fieldError.getDefaultMessage();
	}
}
