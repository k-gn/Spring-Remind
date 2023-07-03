package com.app.pointmanagement;

import org.junit.jupiter.api.AfterEach;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.pointmanagement.domain.message.MessageRepository;
import com.app.pointmanagement.domain.point.PointRepository;
import com.app.pointmanagement.domain.point.reservation.PointReservationRepository;
import com.app.pointmanagement.domain.point.wallet.PointWalletRepository;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BatchTestSupport {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PointWalletRepository pointWalletRepository;

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private PointReservationRepository pointReservationRepository;

	@Autowired
	private MessageRepository messageRepository;

	@AfterEach
	protected void teardown() {
		pointRepository.deleteAllInBatch();
		pointReservationRepository.deleteAllInBatch();
		pointWalletRepository.deleteAllInBatch();
		messageRepository.deleteAllInBatch();
	}

	protected JobExecution launchJob(Job job, JobParameters jobParameters) throws Exception {
		JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
		jobLauncherTestUtils.setJob(job);
		jobLauncherTestUtils.setJobLauncher(jobLauncher);
		jobLauncherTestUtils.setJobRepository(jobRepository);
		return jobLauncherTestUtils.launchJob(jobParameters);
	}
}
