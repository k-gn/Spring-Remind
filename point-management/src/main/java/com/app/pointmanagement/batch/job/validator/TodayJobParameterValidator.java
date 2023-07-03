package com.app.pointmanagement.batch.job.validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class TodayJobParameterValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		if (parameters == null)
			throw new JobParametersInvalidException("job parameter today is required");

		String todayStr = parameters.getString("today");
		if (todayStr == null)
			throw new JobParametersInvalidException("job parameter today is required");

		try {
			LocalDate.parse(todayStr);
		} catch (DateTimeParseException ex) {
			throw new JobParametersInvalidException("job parameter today format is not valid");
		}
	}
}
