package com.example.demo.common.infrastructure;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.common.service.port.UuidHolder;

@Component
public class SystemUuidHolder implements UuidHolder {

	@Override
	public String random() {
		return UUID.randomUUID().toString();
	}
}