package com.app.pointmanagement.batch.job.listener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class InputExpiredPointAlarmCriteriaDateStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		JobParameter todayParameter = stepExecution.getJobParameters().getParameters().get("today");
		if (todayParameter == null) {
			return;
		}
		LocalDate today = LocalDate.parse((String) todayParameter.getValue());
		ExecutionContext context = stepExecution.getExecutionContext();
		context.put("alarmCriteriaDate", today.minusDays(1).format(DateTimeFormatter.ISO_DATE));
		stepExecution.setExecutionContext(context);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
