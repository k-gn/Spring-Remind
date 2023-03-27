package com.example.event.e_2;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderedEventListener {

	/*
		# spring event를 사용하는 이유?
			- 서비스 간의 강한 의존성을 줄이기 위함 (코드의 관심사를 분리)
			- 동기 방식으로 처리하게 되면 전체 프로세스가 끝나는 시간도 짧아지게 됨
	 */

	@Async
	@EventListener // 트랜잭션 안에서 이벤트를 발생시킬 땐 @TransactionalEventListener 를 사용한다.
	public void sendPush(OrderEvent event) throws InterruptedException {
		log.info(String.format("푸시 메세지 발송 [상품명 : %s]", event.getProductName()));
	}

	@Async
	@EventListener
	public void sendMail(OrderEvent event) throws InterruptedException {
		log.info(String.format("메일 전송 [상품명 : %s]", event.getProductName()));
	}
}
