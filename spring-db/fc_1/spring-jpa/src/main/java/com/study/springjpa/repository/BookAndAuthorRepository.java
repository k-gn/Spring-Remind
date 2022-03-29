package com.study.springjpa.repository;

import com.study.springjpa.domain.BookAndAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAndAuthorRepository extends JpaRepository<BookAndAuthor, Long> {

}
