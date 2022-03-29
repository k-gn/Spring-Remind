package com.study.springjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
- JPA 란?
	- 자바 진영의 ORM 표준
- Hibernate 란?
	- JPA에 대한 실제 구현체
- Spring Data Jpa
	- 스프링에서 Hibernate를 좀 더 간편하게 사용하도록 제공하는 것
- ORM 이란?
	- 객체와 데이터 베이스 사이의 관계를 연결해주는 것

- JPA 장단점
    장점
    1) RDB 종류와 관계없이 사용가능하기 때문에 DB 변경이 있어도 코드 재활용이 가능하다.
    2) 기본적인 CRUD 제공과 페이징 처리 등 구현되어 있는 것이 많아 비즈니스 로직에 집중할 수 있다.
        (객체지향적으로 관리가 되기에 비즈니스 로직에만 집중할 수 있기 때문에 생산성에 강점을 가진다.)
    3) 테이블 생성, 변경 등 엔티티 관리가 간편하다.
    4) 쿼리를 직접 작성할 필요 없이 Java 코드로 간편하게 사용할 수 있다.
    5) 쿼리에 로직을 담지 않아도 된다.
    6) 성능 좋음 (지연로딩, 즉시로딩, 1차 캐시)
    단점
    1) 배우기 위한 난이도가 있다. 단방향, 양방향, 임베디드 관계 등 공부할 내용이 많으며, 연관관계에 대한 이해가 없이
        코딩을 하게 되면 성능 문제를 일으킬 수 있고 원하지 않는 결과를 가져올 수 있다.

- MyBatis 장단점
    장점
    1) JPA에 비해 쉽다.
    2) SQL 쿼리를 그대로 사용하기 때문에 복잡한 Join, 튜닝 등을 편하게 작성할 수 있다.
    3) SQL 쿼리의 세부 내용을 변경 시 수월하다.
    4) 동적 쿼리 사용 시 JPA보다 간편하게 구현 가능하다.
    *동적 쿼리: 상황에 따라 분기처리를 통해 SQL문을 동적으로 만드는 기법
    단점
    1) DB 설정 변경 시 수정할 부분이 많다.
    2) Mapper 작성부터, 인터페이스, 모델 설계까지 JPA보다 많은 설계와 파일, 로직이 필요하다.
    -> Mapper에서 select id로 사용할 인터페이스 설계, 쿼리에 파라미터로 # 혹은 $로 파라미터 바인딩을 위한 model 정의, resultType/ParameterType으로 전달할 model 정의가 필요하다.
    3) 특정 DB에 종속적이다. DB가 바뀌면 DB 문법에 맞게 mapper를 전부 수정해야한다.
    4) 쿼리에 로직을 녹여야 하는데 유지보수 하기가 힘들고 테스트도 까다롭다.
 */
@SpringBootApplication
@EnableJpaAuditing
public class SpringJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaApplication.class, args);
    }

}
