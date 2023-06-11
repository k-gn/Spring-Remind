package com.example.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.consumer.domain.FailedEvent;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {
}
