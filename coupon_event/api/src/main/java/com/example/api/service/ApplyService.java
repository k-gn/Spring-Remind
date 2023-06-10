package com.example.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplyService {

	private final CouponRepository couponRepository;

	@Transactional
	public void apply(Long userId) {
		long count = couponRepository.count();

		// 동시 요청이 여러개 들어온다면?
		if (count > 100) {
			return;
		}

		couponRepository.save(new Coupon(userId));
	}
}
