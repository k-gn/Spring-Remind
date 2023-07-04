package com.app.pointmanagement.batch.job.reservation;

import java.time.LocalDate;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.transaction.PlatformTransactionManager;

import com.app.pointmanagement.domain.point.Point;
import com.app.pointmanagement.domain.point.PointRepository;
import com.app.pointmanagement.domain.point.reservation.PointReservation;
import com.app.pointmanagement.domain.point.reservation.PointReservationRepository;
import com.app.pointmanagement.domain.point.wallet.PointWallet;
import com.app.pointmanagement.domain.point.wallet.PointWalletRepository;

@Configuration
public class ExecutePointReservationStepConfig {

	@Bean
	@JobScope
	public Step executePointReservationStep(
		StepBuilderFactory stepBuilderFactory,
		PlatformTransactionManager platformTransactionManager,
		JpaPagingItemReader<PointReservation> executePointReservationItemReader,
		ItemProcessor<PointReservation, Pair<PointReservation, Point>> executePointReservationItemProcessor,
		ItemWriter<Pair<PointReservation, Point>> executePointReservationItemWriter
	) {
		return stepBuilderFactory
			.get("executePointReservationStep")
			.allowStartIfComplete(true) // step 중복 실행 가능
			.transactionManager(platformTransactionManager)
			.<PointReservation, Pair<PointReservation, Point>>chunk(1000)
			.reader(executePointReservationItemReader)
			.processor(executePointReservationItemProcessor)
			.writer(executePointReservationItemWriter)
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<PointReservation> executePointReservationItemReader(
		EntityManagerFactory entityManagerFactory,
		@Value("#{T(java.time.LocalDate).parse(jobParameters[today])}") LocalDate today
	) {
		return new JpaPagingItemReaderBuilder<PointReservation>()
			.name("executePointReservationItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select pr from PointReservation pr where pr.earnedDate = :today and pr.executed = false")
			.parameterValues(Map.of("today", today))
			.pageSize(1000)
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<PointReservation, Pair<PointReservation, Point>> executePointReservationItemProcessor() {
		return reservation -> {
			reservation.execute();
			Point earnedPoint = new Point(
				reservation.getPointWallet(),
				reservation.getAmount(),
				reservation.getEarnedDate(),
				reservation.getExpiredDate()
			);
			PointWallet wallet = reservation.getPointWallet();
			wallet.setAmount(wallet.getAmount().add(earnedPoint.getAmount()));
			return Pair.of(reservation, earnedPoint);
		};
	}

	@Bean
	@StepScope
	public ItemWriter<Pair<PointReservation, Point>> executePointReservationItemWriter(
		PointRepository pointRepository,
		PointReservationRepository pointReservationRepository,
		PointWalletRepository pointWalletRepository
	) {
		return pairs -> {
			pairs.forEach(pair -> {
				pointReservationRepository.save(pair.getFirst());
				pointRepository.save(pair.getSecond());
				pointWalletRepository.save(pair.getSecond().getPointWallet());
			});
		};
	}
}
