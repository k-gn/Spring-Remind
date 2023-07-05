package com.app.pointmanagement.batch.job.reader;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mysema.commons.lang.Assert;

public class ReverseJpaPagingItemReaderBuilder<T> {
	private String name;
	private Function<Pageable, Page<T>> query;
	private int pageSize;
	private Sort sort;

	public ReverseJpaPagingItemReaderBuilder<T> name(String name) {
		this.name = name;
		return this;
	}

	public ReverseJpaPagingItemReaderBuilder<T> query(Function<Pageable, Page<T>> query) {
		this.query = query;
		return this;
	}

	public ReverseJpaPagingItemReaderBuilder<T> pageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public ReverseJpaPagingItemReaderBuilder<T> sort(Sort sort) {
		this.sort = sort;
		return this;
	}

	public ReverseJpaPagingItemReader<T> build() {
		Assert.notNull(this.query, "field query is required");
		Assert.notNull(this.name, "field item reader name is required");
		ReverseJpaPagingItemReader<T> reader = new ReverseJpaPagingItemReader<>();
		reader.setName(this.name);
		reader.setQuery(this.query);
		reader.setPageSize(this.pageSize);
		reader.setSort(this.sort);
		return reader;
	}
}