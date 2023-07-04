package com.app.pointmanagement.batch.job.reservation;

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
import com.app.pointmanagement.domain.point.Point;
import com.app.pointmanagement.domain.point.PointRepository;
import com.app.pointmanagement.domain.point.reservation.PointReservation;
import com.app.pointmanagement.domain.point.reservation.PointReservationRepository;
import com.app.pointmanagement.domain.point.wallet.PointWallet;
import com.app.pointmanagement.domain.point.wallet.PointWalletRepository;

class ExecutePointReservationJobConfigTest extends BatchTestSupport {

	@Autowired
	private Job executePointReservationJob;

	@Autowired
	private PointWalletRepository pointWalletRepository;

	@Autowired
	private PointReservationRepository pointReservationRepository;

	@Autowired
	private PointRepository pointRepository;

	@Test
	void executePointReservationJob() throws Exception {
		// given
		LocalDate earnDate = LocalDate.of(2021, 1, 5);
		PointWallet pointWallet = pointWalletRepository.save(new PointWallet("user1", BigInteger.valueOf(3000)));
		pointReservationRepository.save(new PointReservation(pointWallet, BigInteger.valueOf(1000), earnDate, 10));

		// when
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("today", "2021-01-05")
			.toJobParameters();
		JobExecution jobExecution = launchJob(executePointReservationJob, jobParameters);

		// then
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		List<PointReservation> reservations = pointReservationRepository.findAll();
		assertThat(reservations).hasSize(1);
		assertThat(reservations.get(0).isExecuted()).isTrue();

		List<Point> points = pointRepository.findAll();
		assertThat(points).hasSize(1);
		assertThat(points.get(0).getAmount()).isEqualTo(1000);
		assertThat(points.get(0).getEarnedDate()).isEqualTo(LocalDate.of(2021, 1, 5));
		assertThat(points.get(0).getExpiredDate()).isEqualTo(LocalDate.of(2021, 1, 15));

		List<PointWallet> wallets = pointWalletRepository.findAll();
		assertThat(wallets).hasSize(1);
		assertThat(wallets.get(0).getAmount()).isEqualTo(4000);
	}
}