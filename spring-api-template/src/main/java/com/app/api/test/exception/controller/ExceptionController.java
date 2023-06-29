package com.app.api.test.exception.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.api.test.exception.controller.request.BindExceptionRequest;
import com.app.api.test.exception.controller.request.TypeExceptionEnum;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;

@RestController
@RequestMapping("/api/exception")
public class ExceptionController {

	@GetMapping("/bind-exception")
	public String bindException(@Valid BindExceptionRequest request) {
		return "ok";
	}

	@GetMapping("/type-exception")
	public String typeException(TypeExceptionEnum type) {
		return "ok";
	}

	@GetMapping("/business-exception")
	public String businessException() {
		throw new BusinessException(ErrorCode.TEST);
	}

	@GetMapping
	public String exception() {
		throw new IllegalArgumentException("exception test");
	}
}
