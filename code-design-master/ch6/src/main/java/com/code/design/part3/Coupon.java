package com.code.design.part3;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

	private long id;

	private boolean used;

	private double amount;

	private LocalDate expirationDate;

	public Coupon(
		double amount,
		LocalDate expirationDate
	) {
		this.amount = amount;
		this.expirationDate = expirationDate;
		this.used = false;
	}

	// TDA (Tell Don't Ask)
	// 자율적인 객체
	public void apply() {
		verifyCouponIsAvailable();
		this.used = true;
	}

	// 검증
	private void verifyCouponIsAvailable() {
		verifyExpiration();
		verifyUsed();
	}

	// 만료여부
	private boolean isExpiration() {
		return LocalDate.now().isAfter(expirationDate);
	}

	private void verifyExpiration() {
		if (isExpiration()) {
			throw new IllegalArgumentException("만료된 쿠폰입니다.");
		}
	}

	private void verifyUsed() {
		if (this.used) {
			throw new IllegalArgumentException("이미 사용한 쿠폰입니다.");
		}
	}
}