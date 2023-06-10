package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
