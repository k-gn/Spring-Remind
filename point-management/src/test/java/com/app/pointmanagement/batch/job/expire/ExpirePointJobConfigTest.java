package com.app.pointmanagement.batch.job.expire;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
import com.app.pointmanagement.domain.point.wallet.PointWallet;
import com.app.pointmanagement.domain.point.wallet.PointWalletRepository;

class ExpirePointJobConfigTest extends BatchTestSupport {

	@Autowired
	private Job expirePointJob;

	@Autowired
	private PointWalletRepository pointWalletRepository;

	@Autowired
	private PointRepository pointRepository;

	@Test
	void expirePointJob() throws Exception {
		// given
		LocalDate earnDate = LocalDate.of(2021, 1, 1);
		LocalDate expireDate = LocalDate.of(2021, 1, 3);
		PointWallet pointWallet = pointWalletRepository.save(new PointWallet("user123", BigInteger.valueOf(6000)));
		pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate));
		pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate));
		pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate));

		// when
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("today", "2021-01-04")
			.toJobParameters();
		JobExecution jobExecution = launchJob(expirePointJob, jobParameters);

		// then
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
		List<Point> points = pointRepository.findAll();
		assertThat(points.stream().filter(Point::isExpired).count()).isEqualTo(3);
		PointWallet changedPointWallet = pointWalletRepository.findById(pointWallet.getId()).orElseGet(null);
		assertThat(changedPointWallet).isNotNull();
		assertThat(changedPointWallet.getAmount()).isEqualByComparingTo(BigInteger.valueOf(3000));
	}
}