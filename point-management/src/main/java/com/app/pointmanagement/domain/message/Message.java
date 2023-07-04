package com.app.pointmanagement.domain.message;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
public class Message extends IdEntity {

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "text")
	private String content;

	public static Message createExpiredPointMessage(
		String userId,
		LocalDate expiredDate,
		BigInteger expiredAmount
	) {
		return new Message(
			userId,
			String.format("%s 포인트 만료", expiredAmount),
			String.format("%s 기준 %s 포인트가 만료되었습니다.", expiredDate.format(DateTimeFormatter.ISO_DATE), expiredAmount)
		);
	}
}
