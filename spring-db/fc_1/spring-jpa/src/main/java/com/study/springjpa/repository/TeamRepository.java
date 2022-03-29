package com.study.springjpa.repository;

import com.study.springjpa.domain.Member;
import com.study.springjpa.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    
}
