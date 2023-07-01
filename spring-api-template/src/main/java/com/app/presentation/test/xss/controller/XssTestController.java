package com.app.presentation.test.xss.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.presentation.test.xss.dto.XssTest;

@RestController
@RequestMapping("/xss")
public class XssTestController {

	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public XssTest xssTest1(XssTest xssTestDto) {
		return xssTestDto;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public XssTest xssTest2(@RequestBody XssTest xssTestDto) {
		return xssTestDto;
	}

}
