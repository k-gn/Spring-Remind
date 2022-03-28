package com.study.springjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
- JPA 란?
	- 자바 진영의 ORM 표준
- Hibernate 란?
	- JPA에 대한 실제 구현체
- Spring Data Jpa
	- 스프링에서 Hibernate를 좀 더 간편하게 사용하도록 제공하는 것
- ORM 이란?
	- 객체와 데이터 베이스 사이의 관계를 연결해주는 것
 */
@SpringBootApplication
public class SpringJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaApplication.class, args);
    }

}
