package com.example.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplyService {

	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;

	@Transactional
	public void apply(Long userId) {
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

		couponRepository.save(new Coupon(userId));
	}
}
