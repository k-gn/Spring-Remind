package com.example.event.e_2;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventService {

	private final ApplicationEventPublisher publisher;

	public void order(String productName) {
		log.info(String.format("주문 로직 처리 [상품명 : %s]", productName));
		// 이벤트 발행 -> 이벤트 리스너 동작
		publisher.publishEvent(new OrderEvent(productName));
	}
}
