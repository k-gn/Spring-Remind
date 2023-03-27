package com.example.event.e_2;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderedEventListener {

	@Async
	@EventListener
	public void sendPush(OrderEvent event) throws InterruptedException {
		log.info(String.format("푸시 메세지 발송 [상품명 : %s]", event.getProductName()));
	}

	@Async
	@EventListener
	public void sendMail(OrderEvent event) throws InterruptedException {
		log.info(String.format("메일 전송 [상품명 : %s]", event.getProductName()));
	}
}
