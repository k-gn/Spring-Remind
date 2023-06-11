package com.example.consumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.consumer.domain.Coupon;
import com.example.consumer.domain.FailedEvent;
import com.example.consumer.repository.CouponRepository;
import com.example.consumer.repository.FailedEventRepository;

@Component
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;
	private final FailedEventRepository failedEventRepository;
	private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);

	public CouponCreatedConsumer(
		CouponRepository couponRepository,
		FailedEventRepository failedEventRepository
	) {
		this.couponRepository = couponRepository;
		this.failedEventRepository = failedEventRepository;
	}

	@KafkaListener(topics = "coupon_create", groupId = "group_1")
	public void listener(Long userId) {
		try {
			couponRepository.save(new Coupon(userId));
		} catch (Exception e) {
			// 쿠폰 발급 실패 처리
			logger.error("failed to create coupon::" + userId);
			// FailedEvent를 통해 배치 프로그램을 사용하여 쿠폰 재발급을 해줄 수 있다.
			failedEventRepository.save(new FailedEvent(userId));
		}
	}
}
