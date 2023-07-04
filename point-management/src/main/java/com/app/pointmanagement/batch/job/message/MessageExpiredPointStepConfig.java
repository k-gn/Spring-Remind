package com.app.pointmanagement.batch.job.message;

import java.time.LocalDate;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.transaction.PlatformTransactionManager;

import com.app.pointmanagement.batch.job.listener.InputExpiredPointAlarmCriteriaDateStepListener;
import com.app.pointmanagement.domain.message.Message;
import com.app.pointmanagement.domain.point.ExpiredPointSummary;
import com.app.pointmanagement.domain.point.Point;
import com.app.pointmanagement.domain.point.PointRepository;
import com.app.pointmanagement.domain.point.reservation.PointReservation;
import com.app.pointmanagement.domain.point.reservation.PointReservationRepository;
import com.app.pointmanagement.domain.point.wallet.PointWallet;
import com.app.pointmanagement.domain.point.wallet.PointWalletRepository;

@Configuration
public class MessageExpiredPointStepConfig {

	@Bean
	@JobScope
	public Step messageExpiredPointStep(
		StepBuilderFactory stepBuilderFactory,
		PlatformTransactionManager platformTransactionManager,
		InputExpiredPointAlarmCriteriaDateStepListener listener,
		RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader,
		ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor,
		JpaItemWriter<Message> messageExpiredPointItemWriter
	) {
		return stepBuilderFactory
			.get("messageExpiredPointStep")
			.allowStartIfComplete(true)
			.transactionManager(platformTransactionManager)
			.listener(listener)
			.<ExpiredPointSummary, Message>chunk(1000)
			.reader(messageExpiredPointItemReader)
			.processor(messageExpiredPointItemProcessor)
			.writer(messageExpiredPointItemWriter)
			.build();
	}

	@Bean
	@StepScope
	public RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader(
		PointRepository pointRepository,
		@Value("#{T(java.time.LocalDate).parse(stepExecutionContext[alarmCriteriaDate])}") LocalDate alarmCriteriaDate
	) {
		return new RepositoryItemReaderBuilder<ExpiredPointSummary>()
			.name("messageExpiredPointItemReader")
			.repository(pointRepository)
			.methodName("sumByExpiredDate")
			.pageSize(1000)
			.arguments(alarmCriteriaDate)
			.sorts(Map.of("pointWallet", Sort.Direction.ASC))
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor(
		@Value("#{T(java.time.LocalDate).parse(jobParameters[today])}") LocalDate today
	) {
		return expiredPointSummary -> Message.createExpiredPointMessage(
			expiredPointSummary.getUserId(),
			today,
			expiredPointSummary.getAmount()
		);
	}

	@Bean
	@StepScope
	public JpaItemWriter<Message> messageExpiredPointItemWriter(EntityManagerFactory entityManagerFactory) {
		// 어느정도 쌓이면 DB에 일괄적으로 저장한다.
		JpaItemWriter<Message> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		return jpaItemWriter;
	}
}
