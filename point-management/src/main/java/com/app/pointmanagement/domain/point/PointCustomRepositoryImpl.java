package com.app.pointmanagement.domain.point;

import static com.app.pointmanagement.domain.point.QPoint.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointCustomRepositoryImpl implements PointCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<ExpiredPointSummary> sumByExpiredDate(
		LocalDate alarmCriteriaDate,
		Pageable pageable
	) {
		List<ExpiredPointSummary> expiredPointSummaries = queryFactory
			.select(
				new QExpiredPointSummary(
					point.pointWallet.userId,
					point.amount.sum().coalesce(BigInteger.ZERO).as("amount")
				)
			)
			.from(point)
			.where(
				point.expired.eq(true),
				point.used.eq(false),
				point.expiredDate.eq(alarmCriteriaDate)
			)
			.groupBy(point.pointWallet)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long elementCount = queryFactory
			.select(point.count())
			.from(point)
			.where(
				point.expired.eq(true),
				point.used.eq(false),
				point.expiredDate.eq(alarmCriteriaDate)
			)
			.groupBy(point.pointWallet)
			.fetch().size();

		return new PageImpl<>(expiredPointSummaries, pageable, elementCount);
	}
}
