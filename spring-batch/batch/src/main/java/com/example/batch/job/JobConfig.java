package com.example.batch.job;

import java.util.concurrent.TimeUnit;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
/*
	# Job
		- 배치작업에 필요한 데이터를 파라미터로 받아 실행된다.
		- Step의 순서를 정의
		- 전체 배치 프로세스를 캡슐화한 도메인

	# Step
		- 작업 처리 단위
		- Chunk 기반 스텝, Tasklet 스텝 2가지로 나뉜다.

		- Chunk 기반 스텝 : 하나의 트랜잭션에서 chunkSize만큼 데이터 처리 (read-process-write)
			- chunkSize : 한 트랜잭션에서 쓸 아이템 갯수
			- commitInterval : reader가 한번에 읽을 아이템 갯수
			- chunkSize >= commitInterval (일반적으론 =)
		- Tasklet 스텝 : 하나의 트랜잭션에서 데이터 처리 (내부에서 단순 처리)
			- RepeatStatus (반복상태)를 설정한다.
 */
@Slf4j
public class JobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean("helloJob")
	public Job helloJob(Step helloStep) {
		return jobBuilderFactory.get("helloJob")
			.incrementer(new RunIdIncrementer())
			.start(helloStep)
			.build();
	}

	@JobScope // job이 실행되는 동안에만 빈이 실행됨
	@Bean("helloStep")
	public Step helloStep(Tasklet tasklet) {
		return stepBuilderFactory.get("helloStep")
			.tasklet(tasklet)
			.build();
	}

	@StepScope
	@Bean
	public Tasklet tasklet() {
		return (contribution, chunkContext) -> {
			log.info("Hello Spring Batch");
			return RepeatStatus.FINISHED;
		};
	}
}
