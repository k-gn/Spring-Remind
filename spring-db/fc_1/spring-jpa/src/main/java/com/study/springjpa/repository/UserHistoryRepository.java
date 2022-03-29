package com.study.springjpa.repository;

import com.study.springjpa.domain.Book;
import com.study.springjpa.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    List<UserHistory> findByUserId(Long userId);
}