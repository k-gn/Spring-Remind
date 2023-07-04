package com.app.pointmanagement.batch.job.message;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.pointmanagement.batch.job.validator.TodayJobParameterValidator;

@Configuration
public class MessageExpiredPointJobConfig {

	@Bean
	public Job messageExpiredPointJob(
		JobBuilderFactory jobBuilderFactory,
		TodayJobParameterValidator todayJobParameterValidator,
		Step messageExpiredPointStep
	) {
		return jobBuilderFactory
			.get("messageExpiredPointJob")
			.validator(todayJobParameterValidator)
			.start(messageExpiredPointStep)
			.build();
	}
}
