package com.example.batch.job;

import java.util.concurrent.TimeUnit;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job simpleJob() {
		return jobBuilderFactory
			.get("simple-job")
			.start(simpleStep())
			.build();
	}

	@Bean
	public Step simpleStep() {
		return this.stepBuilderFactory
			.get("simple-step")
			.tasklet(simpleTasklet())
			.build();
	}

	@Bean
	public Tasklet simpleTasklet() {
		return (stepContribution, chunkContext) -> {
			for (int i = 10; i > 0; i--) {
				TimeUnit.SECONDS.sleep(1);
				System.out.println(i);
			}
			return RepeatStatus.FINISHED;
		};
	}
}
