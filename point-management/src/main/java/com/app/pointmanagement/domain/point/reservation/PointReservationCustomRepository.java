package com.app.pointmanagement.domain.point.reservation;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointReservationCustomRepository {

	Page<PointReservation> findPointReservationToExecute(LocalDate today, Pageable pageable);
	Page<PointReservation> findPointReservationToExecute(LocalDate today, Long minId, Long maxId, Pageable pageable);

	Long findMinId(LocalDate today);

	Long findMaxId(LocalDate today);
}
