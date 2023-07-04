package com.app.pointmanagement.batch.job.message;

import static org.assertj.core.api.Assertions.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.pointmanagement.BatchTestSupport;
import com.app.pointmanagement.domain.message.Message;
import com.app.pointmanagement.domain.message.MessageRepository;
import com.app.pointmanagement.domain.point.Point;
import com.app.pointmanagement.domain.point.PointRepository;
import com.app.pointmanagement.domain.point.wallet.PointWallet;
import com.app.pointmanagement.domain.point.wallet.PointWalletRepository;

class MessageExpiredPointJobConfigTest extends BatchTestSupport {

	@Autowired
	private Job messageExpiredPointJob;

	@Autowired
	private PointWalletRepository pointWalletRepository;

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Test
	void messageExpiredPointJob() throws Exception {
		// given
		LocalDate earnDate = LocalDate.of(2021, 1, 1);
		LocalDate expireDate = LocalDate.of(2021, 9, 5);
		LocalDate notExpireDate = LocalDate.of(2025, 12, 31);
		PointWallet pointWallet1 = pointWalletRepository.save(new PointWallet("user1", BigInteger.valueOf(3000)));
		PointWallet pointWallet2 = pointWalletRepository.save(new PointWallet("user2", BigInteger.ZERO));
		pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
		pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
		pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
		pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
		pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
		pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
		pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
		pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));

		// when
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("today", "2021-09-06")
			.toJobParameters();
		JobExecution jobExecution = launchJob(messageExpiredPointJob, jobParameters);

		// then
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		List<Message> messages = messageRepository.findAll();
		assertThat(messages).hasSize(2);

		Message msg1 = messages.stream().filter(item -> item.getUserId().equals("user1")).findFirst().orElse(null);
		assertThat(msg1).isNotNull();
		assertThat(msg1.getTitle()).isEqualTo("3000 포인트 만료");
		assertThat(msg1.getContent()).isEqualTo("2021-09-06 기준 3000 포인트가 만료되었습니다.");

		Message msg2 = messages.stream().filter(item -> item.getUserId().equals("user2")).findFirst().orElse(null);
		assertThat(msg2).isNotNull();
		assertThat(msg2.getTitle()).isEqualTo("2000 포인트 만료");
		assertThat(msg2.getContent()).isEqualTo("2021-09-06 기준 2000 포인트가 만료되었습니다.");
	}
}