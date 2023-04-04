package com.example.rest_docs.error;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

	private String message;
	private int status;
	private List<FieldError> errors;
	private String code;
	private LocalDateTime timestamp;

	private ErrorResponse(
		final ErrorCode code,
		final List<FieldError> errors
	) {
		this.message = code.getMessage();
		this.status = code.getStatus();
		this.errors = errors;
		this.code = code.getCode();
		this.timestamp = LocalDateTime.now();
	}

	private ErrorResponse(final ErrorCode code) {
		this.message = code.getMessage();
		this.status = code.getStatus();
		this.code = code.getCode();
		this.errors = new ArrayList<>();
		this.timestamp = LocalDateTime.now();
	}

	public static ErrorResponse of(
		final ErrorCode code,
		final BindingResult bindingResult
	) {
		return new ErrorResponse(code, FieldError.of(bindingResult));
	}

	public static ErrorResponse of(final ErrorCode code) {
		return new ErrorResponse(code);
	}

	/*
		# why "static" inner class?
		1. 외부 참조가 유지된다는 것은 메모리에 대한 참조가 유지되고 있다는 뜻 -> GC가 메모리를 회수할 수 없다. 당연히 이는 메모리 누수를 부르는 치명적인 단점이다.
		2. 항상 외부 인스턴스의 참조를 통해야 하므로 성능 상 비효율적이다.
	 */
	@Getter
	public static class FieldError {

		private String field;
		private String value;
		private String reason;

		private FieldError(
			final String field,
			final String value,
			final String reason
		) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		public static List<FieldError> of(
			final String field,
			final String value,
			final String reason
		) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, value, reason));
			return fieldErrors;
		}

		private static List<FieldError> of(final BindingResult bindingResult) {
			final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			return fieldErrors.stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.collect(Collectors.toList());
		}
	}
}
