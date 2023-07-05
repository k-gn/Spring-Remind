package com.app.pointmanagement.batch.job.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ReverseJpaPagingItemReader<T> extends ItemStreamSupport implements ItemReader<T> {
	private static final int DEFAULT_PAGE_SIZE = 100;

	private int page = 0;
	private int totalPage = 0;
	private List<T> readRows = new ArrayList<>();

	private int pageSize = DEFAULT_PAGE_SIZE;
	private Function<Pageable, Page<T>> query;
	private Sort sort = Sort.unsorted();

	ReverseJpaPagingItemReader() {}

	public void setPageSize(int pageSize) {
		this.pageSize = (pageSize > 0) ? pageSize : DEFAULT_PAGE_SIZE;
	}

	public void setQuery(Function<Pageable, Page<T>> query) {
		this.query = query;
	}

	public void setSort(Sort sort) {
		if (!Objects.isNull(sort)) {
			// pagination을 마지막 페이지부터 하기때문에 sort direction를 모두 reverse한다.
			// ASC <-> DESC
			Iterator<Sort.Order> orderIterator = sort.iterator();
			final List<Sort.Order> reverseOrders = new LinkedList<>();
			while (orderIterator.hasNext()) {
				Sort.Order prev = orderIterator.next();
				reverseOrders.add(
					new Sort.Order(
						prev.getDirection().isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC,
						prev.getProperty()
					)
				);
			}
			this.sort = Sort.by(reverseOrders);
		}
	}

	/**
	 * 스텝 실행 전에 동작함
	 */
	@BeforeStep
	public void beforeStep() {
		// 우리는 뒤에서부터 읽을 것이기 때문에 마지막 페이지 번호를 구해서 page에 넣어줍니다.
		totalPage = getTargetData(0).getTotalPages();
		page = totalPage - 1;
	}

	/**
	 * read() 함수가 실행되기 전에 동작함
	 */
	@BeforeRead
	public void beforeRead() {
		if (page < 0)
			return;
		if (readRows.isEmpty()) // 읽은 데이터를 모두 소진하면 db로 부터 데이터를 가져와서 채워놓는다.
			readRows = new ArrayList<>(getTargetData(page).getContent());
	}

	@Override
	public T read() {
		// null을 반환하면 Reader는 모든 데이터를 소진한걸로 인지하고 종료합니다.
		// 데이터를 리스트에서 거꾸로 (readRows.size() - 1) 뒤에서부터 빼줍니다.
		return readRows.isEmpty() ? null : readRows.remove(readRows.size() - 1);
	}

	/**
	 * read() 이후에 동작함
	 */
	@AfterRead
	public void afterRead() {
		// 데이터가 없다면 page를 1 차감합니다.
		if (readRows.isEmpty()) {
			this.page--;
		}
	}

	/**
	 * page 번호에 해당하는 데이터를 가져와서 Page 형식으로 반환합니다.
	 */
	private Page<T> getTargetData(int readPage) {
		return Objects.isNull(query) ? Page.empty() : query.apply(PageRequest.of(readPage, pageSize, sort));
	}
}