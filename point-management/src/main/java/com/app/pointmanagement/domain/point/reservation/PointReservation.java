package com.app.pointmanagement.domain.point.reservation;

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
public class PointReservation extends IdEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "point_wallet_id", nullable = false)
	private PointWallet pointWallet;

	@Column(name = "amount", nullable = false, columnDefinition = "BIGINT")
	private BigInteger amount;

	@Column(name = "eared_date", nullable = false)
	private LocalDate earedDate; // 언제 적립할 건지

	@Column(name = "available_days", nullable = false)
	private int availableDays; // 적립한 일 + 유효일 = 만료일

	@Column(name = "is_expired", nullable = false)
	private boolean executed;

	public PointReservation(
		PointWallet pointWallet,
		BigInteger amount,
		LocalDate earedDate,
		int availableDays
	) {
		this.pointWallet = pointWallet;
		this.amount = amount;
		this.earedDate = earedDate;
		this.availableDays = availableDays;
	}

	public void execute() {
		this.executed = true;
	}

	public LocalDate getExpiredDate() {
		return this.earedDate.plusDays(this.availableDays);
	}
}
