package com.app.pointmanagement.domain.point;

import java.math.BigInteger;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.app.pointmanagement.domain.common.IdEntity;
import com.app.pointmanagement.domain.point.wallet.PointWallet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point extends IdEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "point_wallet_id", nullable = false)
	private PointWallet pointWallet;

	@Column(name = "amount", nullable = false, columnDefinition = "BIGINT")
	private BigInteger amount;

	@Column(name = "eared_date", nullable = false)
	private LocalDate earedDate;

	@Column(name = "expired_date", nullable = false)
	private LocalDate expiredDate;

	@Column(name = "is_used", nullable = false)
	private boolean used;

	@Column(name = "is_expired", nullable = false)
	private boolean expired;

	public Point(
		PointWallet pointWallet,
		BigInteger amount,
		LocalDate earedDate,
		LocalDate expiredDate
	) {
		this.pointWallet = pointWallet;
		this.amount = amount;
		this.earedDate = earedDate;
		this.expiredDate = expiredDate;
	}

	public void expire() {
		if (!this.used)
			this.expired = true;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
}
