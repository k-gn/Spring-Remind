package com.app.pointmanagement.batch.job.expire;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.pointmanagement.batch.job.validator.TodayJobParameterValidator;

@Configuration
public class ExpirePointJobConfig {

	@Bean
	public Job expirePointJob(
		JobBuilderFactory jobBuilderFactory,
		TodayJobParameterValidator todayJobParameterValidator,
		Step expirePointStep
	) {
		return jobBuilderFactory.get("expirePointJob")
			.start(expirePointStep)
			.validator(todayJobParameterValidator)
			.incrementer(new RunIdIncrementer()) // run.id 계속 증가 -> Job 중복 실행 가능 (기존엔 이전 job과 동일한 경우 실행시키지 않음)
			.build();
	}
}
