package com.app.pointmanagement.domain.point.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointReservationRepository extends JpaRepository<PointReservation, Long>, PointReservationCustomRepository {
}
