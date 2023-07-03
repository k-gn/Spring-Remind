package com.app.pointmanagement.domain.point.wallet;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.app.pointmanagement.domain.common.IdEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PointWallet extends IdEntity {

	@Column(name = "user_id", unique = true, nullable = false)
	private String userId;

	@Column(name = "amount", columnDefinition = "BIGINT")
	private BigInteger amount;

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}
}
