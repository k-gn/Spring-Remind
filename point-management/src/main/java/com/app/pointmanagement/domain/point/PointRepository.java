package com.app.pointmanagement.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.pointmanagement.domain.message.Message;

public interface PointRepository extends JpaRepository<Point, Long> {
}
