package com.example.batch.job;

import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import com.example.batch.core.domain.PlainText;
import com.example.batch.core.repository.PlainTextRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class PlainTextJobConfig {

	/*
		데이터 읽기 : ItemReader
		데이터 처리하기 : ItemProcessor
		데이터 쓰기 : ItemWriter
	 */

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final PlainTextRepository plainTextRepository;

	@Bean("plainTextJob")
	public Job plainTextJob(Step plainTextStep) {
		return jobBuilderFactory.get("plainTextJob")
			.incrementer(new RunIdIncrementer())
			.start(plainTextStep)
			.build();
	}

	@JobScope
	@Bean("plainTextStep")
	public Step plainTextStep(
		ItemReader plainTextReader,
		ItemProcessor plainTextProcessor,
		ItemWriter plainTextWriter
	) {
		return stepBuilderFactory.get("plainTextStep")
			.<PlainText, String>chunk(5)
			.reader(plainTextReader)
			.processor(plainTextProcessor)
			.writer(plainTextWriter)
			.build();
	}

	@StepScope
	@Bean
	public RepositoryItemReader<PlainText> plainTextReader() {
		return new RepositoryItemReaderBuilder<PlainText>()
			.name("plainTextReader")
			.repository(plainTextRepository)
			.methodName("findBy")
			.pageSize(5) // commitInterval
			.arguments(List.of())
			.sorts(Collections.singletonMap("id", Sort.Direction.DESC))
			.build();
	}

	@StepScope
	@Bean
	public ItemProcessor<PlainText, String> plainTextProcessor() {
		return item -> "processed" + item.getText();
	}

	@StepScope
	@Bean
	public ItemWriter<String> plainTextWriter() {
		return items -> {
			items.forEach(System.out::println);
			System.out.println("==== chunk is finished");
		};
	}
}
