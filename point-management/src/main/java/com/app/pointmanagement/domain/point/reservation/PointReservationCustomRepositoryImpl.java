package com.app.pointmanagement.domain.point.reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.JPQLQuery;

public class PointReservationCustomRepositoryImpl extends QuerydslRepositorySupport
	implements PointReservationCustomRepository {
	public PointReservationCustomRepositoryImpl() {
		super(PointReservation.class);
	}

	@Override
	public Page<PointReservation> findPointReservationToExecute(
		LocalDate today,
		Pageable pageable
	) {
		QPointReservation pointReservation = QPointReservation.pointReservation;
		JPQLQuery<PointReservation> query = from(pointReservation)
			.select(pointReservation)
			.where(pointReservation.earnedDate.loe(today))
			.where(pointReservation.executed.eq(false));
		List<PointReservation> points = getQuerydsl().applyPagination(pageable, query).fetch();
		long elementCount = query.fetchCount();
		return new PageImpl<>(
			points,
			PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
			elementCount
		);
	}

	@Override
	public Page<PointReservation> findPointReservationToExecute(
		LocalDate today,
		Long minId,
		Long maxId,
		Pageable pageable
	) {
		QPointReservation pointReservation = QPointReservation.pointReservation;
		JPQLQuery<PointReservation> query = from(pointReservation)
			.select(pointReservation)
			.where(pointReservation.earnedDate.loe(today))
			.where(pointReservation.executed.eq(false))
			.where(pointReservation.id.goe(minId))
			.where(pointReservation.id.loe(maxId));
		List<PointReservation> points = getQuerydsl().applyPagination(pageable, query).fetch();
		long elementCount = query.fetchCount();
		return new PageImpl<>(
			points,
			PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
			elementCount
		);
	}

	@Override
	public Long findMinId(LocalDate today) {
		QPointReservation pointReservation = QPointReservation.pointReservation;
		return from(pointReservation)
			.select(pointReservation.id.min())
			.where(pointReservation.earnedDate.loe(today))
			.where(pointReservation.executed.eq(false))
			.fetchOne();
	}

	@Override
	public Long findMaxId(LocalDate today) {
		QPointReservation pointReservation = QPointReservation.pointReservation;
		return from(pointReservation)
			.select(pointReservation.id.max())
			.where(pointReservation.earnedDate.loe(today))
			.where(pointReservation.executed.eq(false))
			.fetchOne();
	}
}
