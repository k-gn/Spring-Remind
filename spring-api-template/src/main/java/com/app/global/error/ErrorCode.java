package com.app.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	TEST("001", HttpStatus.INTERNAL_SERVER_ERROR, "business exception test")
	;

	private final String code;
	private final HttpStatus status;
	private final String message;
}
