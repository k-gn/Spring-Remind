package com.example.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.consumer.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
