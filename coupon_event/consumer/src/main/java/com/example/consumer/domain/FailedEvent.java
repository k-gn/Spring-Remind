package com.example.consumer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/*
	# 쿠폰을 발급하다가 에러가 발생한다면?
	- 백업 데이터와 로그를 남겨 해결할 수 있다.
 */
@Entity
public class FailedEvent {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	public FailedEvent() {
	}

	public FailedEvent(Long userId) {
		this.userId = userId;
	}
}
