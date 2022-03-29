package com.study.springjpa.repository;

import com.study.springjpa.domain.BookReviewInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewInfoRepository extends JpaRepository<BookReviewInfo, Long> {

}
