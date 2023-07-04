package com.app.pointmanagement.domain.point;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointCustomRepository {

	Page<ExpiredPointSummary> sumByExpiredDate(
		LocalDate alarmCriteriaDate,
		Pageable pageable
	);
}
