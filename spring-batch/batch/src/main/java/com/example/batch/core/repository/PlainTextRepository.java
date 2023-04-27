package com.example.batch.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.batch.core.domain.PlainText;

public interface PlainTextRepository extends JpaRepository<PlainText, Integer> {

	Page<PlainText> findBy(Pageable pageable);
}
