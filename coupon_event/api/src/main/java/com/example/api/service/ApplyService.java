package com.example.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplyService {

	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;
	private final CouponCreateProducer couponCreateProducer;
	private final AppliedUserRepository appliedUserRepository;

	// @Transactional
	public void apply(Long userId) {
		/*
			# 발급가능한 쿠폰 개수를 1인당 1개로 제한한다면?
			1. 데이터베이스 단에서 유니크 키로 해결하기 (요구사항에 따라 실용적이진 않음)
			2. Lock 활용하기 (성능 이슈가 있을 수 있음)
			3. set 자료구조 활용하기 (redis)
		 */
		Long apply = appliedUserRepository.add(userId);
		if (apply != 1) {
			return;
		}

		/*
			- redis incr : key에 대한 value를 1씩 증가시킨다. (성능도 빠름)
				- redis는 싱글스레드 기반으로 동작
				- 단, 발급하는 쿠폰 갯수가 많아질수록 db에 부하를 줄 수 있고, 다른 서비스에 영향을 미칠 수 있다.
					- 타임아웃, 서비스 지연 등
			- 쿠폰 갯수에 대한 정합성이 중요하다.
			- 락을 건다면 성능상 불이익이 있을 수 있다.
		 */
		// long count = couponRepository.count();
		long count = couponCountRepository.increment();



		// 동시 요청이 여러개 들어온다면?
		if (count > 100) {
			return;
		}

		// couponRepository.save(new Coupon(userId));
		// kafka 적용 -> 쿠폰 처리량 조절 가능 -> db 부하를 줄일 수 있다.
		// https://www.inflearn.com/questions/899360/kafka-%EB%93%B1%EC%9D%98-%EB%A9%94%EC%8B%9C%EC%A7%80-%EB%B8%8C%EB%A1%9C%EC%BB%A4%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0%EC%97%90-%EB%8C%80%ED%95%B4-%EC%A7%88%EB%AC%B8%EC%9E%88%EC%8A%B5%EB%8B%88%EB%8B%A4
		couponCreateProducer.create(userId);
	}
}
