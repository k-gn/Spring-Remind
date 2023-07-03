package com.app.pointmanagement.batch.job.reservation;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.pointmanagement.batch.job.validator.TodayJobParameterValidator;

@Configuration
public class ExecutePointReservationJobConfig {

	@Bean
	public Job executePointReservationJob(
		JobBuilderFactory jobBuilderFactory,
		TodayJobParameterValidator todayJobParameterValidator,
		Step executePointReservationStep
	) {
		return jobBuilderFactory.get("executePointReservationJob")
			.validator(todayJobParameterValidator)
			.incrementer(new RunIdIncrementer())
			.start(executePointReservationStep)
			.build();
	}
}
