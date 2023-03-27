package com.example.event.e_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	private final PushService pushService;
	private final MailService mailService;

	@Autowired
	public OrderService(PushService pushService, MailService mailService) {
		this.pushService = pushService;
		this.mailService = mailService;
	}
}
