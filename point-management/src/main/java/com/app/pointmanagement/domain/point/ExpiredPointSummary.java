package com.app.pointmanagement.domain.point;

import java.math.BigInteger;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ExpiredPointSummary {

	private String userId;
	private BigInteger amount;

	@QueryProjection
	public ExpiredPointSummary(
		String userId,
		BigInteger amount
	) {
		this.userId = userId;
		this.amount = amount;
	}
}
