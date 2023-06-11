package com.example.api.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AppliedUserRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public Long add(Long userId) {
		// redis 는 set 에 값을 넣을 때 1을 리턴하고, 이미 존재하면 0을 리턴한다.
		return redisTemplate.opsForSet().add("applied_user", userId.toString());
	}
}
