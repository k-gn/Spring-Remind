package com.app.pointmanagement.batch.job.expire;

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
import org.springframework.transaction.PlatformTransactionManager;

import com.app.pointmanagement.domain.point.Point;
import com.app.pointmanagement.domain.point.PointRepository;
import com.app.pointmanagement.domain.point.wallet.PointWallet;
import com.app.pointmanagement.domain.point.wallet.PointWalletRepository;

@Configuration
public class ExpirePointStepConfig {

	@Bean
	@JobScope
	public Step expirePointStep(
		StepBuilderFactory stepBuilderFactory,
		PlatformTransactionManager platformTransactionManager,
		JpaPagingItemReader<Point> expirePointItemReader,
		ItemProcessor<Point, Point> expirePointItemProcessor,
		ItemWriter<Point> expirePointItemWriter
	) {
		return stepBuilderFactory
			.get("expirePointStep")
			.allowStartIfComplete(true)
			.transactionManager(platformTransactionManager)
			.<Point, Point>chunk(1000)
			.reader(expirePointItemReader)
			.processor(expirePointItemProcessor)
			.writer(expirePointItemWriter)
			.build();
	}

	/*
		1. 처리할 데이터 개수 > Page Size 인 경우
		2. Reader 조건으로 사용되는 필드와 Processor 에서 수정하는 필드가 동일한 경우
		=> 위 두 조건을 만족할 때 문제가 발생할 수 있다.
			=> 페이지를 잘못 쪼개서 가져온다.

		=> 순서를 거꾸로 뒤집어서 데이터를 가져오면 해결할 수 있다.

		- Reader 조회 성능은 배치 성능에 가장 큰 영향을 미치는 것중 하나다.
		=> 쿼리 성능이 중요하다. (쿼리 튜닝 or 인덱스)
	 */
	@Bean
	@StepScope
	public JpaPagingItemReader<Point> expirePointItemReader(
		EntityManagerFactory entityManagerFactory,
		@Value("#{T(java.time.LocalDate).parse(jobParameters[today])}") LocalDate today
	) {
		return new JpaPagingItemReaderBuilder<Point>()
			.name("expirePointItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select p from Point p where p.expiredDate < :today and used = false and expired = false")
			.parameterValues(Map.of("today", today))
			.pageSize(1000)
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<Point, Point> expirePointItemProcessor() {
		return point -> {
			point.expire();
			PointWallet wallet = point.getPointWallet();
			wallet.setAmount(wallet.getAmount().subtract(point.getAmount()));
			return point;
		};
	}

	@Bean
	@StepScope
	public ItemWriter<Point> expirePointItemWriter(
		PointRepository pointRepository,
		PointWalletRepository pointWalletRepository
	) {
		return points -> {
			for (Point point : points) {
				if (point.isExpired()) {
					pointRepository.save(point);
					pointWalletRepository.save(point.getPointWallet());
				}
			}
		};
	}
}