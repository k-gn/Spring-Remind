package com.example.batchsize.domain.pk_none_strategy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.batchsize.domain.pk_identity_strategy.Person;

public interface MemberRepository extends JpaRepository<Member, String> {
}
