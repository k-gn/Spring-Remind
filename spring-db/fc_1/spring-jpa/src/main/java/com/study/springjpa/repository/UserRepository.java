package com.study.springjpa.repository;

import com.study.springjpa.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

// extends JpaRepository<User, Long> 를 상속만 받아도 jpa 의 쉽게 다양한 기능을 사용할 수 있다.
// 제네릭 : Entity, PK
public interface UserRepository extends JpaRepository<User, Long> {

    // 쿼리 메소드
    // 네이밍 규칙에 맞게 작성 (문서에 잘 나와있음)
    // 리턴 타입은 개발자가 정의해준대로 자동으로 변환하여 반환해준다. (문서에 리턴 타입 잘 나와있음)
    // 쿼리 메소드는 꼭 테스트를 해보자!
    /*

        조회 : find...By

        COUNT : count...By

        EXISTS : exists...By

        삭제 : delete...By

        DISTINCT : findDistinct

        LIMIT : findFirst3, findFirst1, findTop1

    */
//    List<User> findByName(String name);
    Optional<User> findByName(String name);

    // 코드 가독성 up (별 의미는 없다)
    Set<User> findUserByNameIs(String name);
    Set<User> findUserByName(String name);
    Set<User> findUserByNameEquals(String name);

    User findByEmail(String email);

    User getByEmail(String email);

    User readByEmail(String email);

    User queryByEmail(String email);

    User searchByEmail(String email);

    User streamByEmail(String email);

    User findUserByEmail(String email);

    User findSomethingByEmail(String email);

    // 네이밍 오류 시 런타임 에러
    Long countByName(String name);
    boolean existsByName(String name);

    // limit
    List<User> findFirst2ByName(String name);

    List<User> findTop2ByName(String name);

    // Last1 같이 없는 규칙은 무시된다.
    List<User> findLast1ByName(String name);

    // and
    List<User> findByEmailAndName(String email, String name);

    // or
    List<User> findByEmailOrName(String email, String name);

    // CreatedAt > ?
    List<User> findByCreatedAtAfter(LocalDateTime yesterday);
    // id > ?
    List<User> findByIdAfter(Long id);
    // CreatedAt > ?
    List<User> findByCreatedAtGreaterThan(LocalDateTime yesterday); // <-> LessThen

    // CreatedAt >= ?
    List<User> findByCreatedAtGreaterThanEqual(LocalDateTime yesterday);

    // between ? and ? (양 끝값 포함)
    List<User> findByCreatedAtBetween(LocalDateTime yesterday, LocalDateTime tomorrow);
    List<User> findByIdBetween(Long id1, Long id2);
    // id1 >= ? and id2 <= ?
    List<User> findByIdGreaterThanEqualAndIdLessThanEqual(Long id1, Long id2);

    // is not null
    List<User> findByIdIsNotNull();

    // NotEmpty : collection properties 에 사용가능, exists 문으로 검색 (잘 안쓴다.)
//    List<User> findByAddressIsNotEmpty();   // name is not null and name != '' 의미가 아니다.
    // ...True, ...False

    // in
    List<User> findByNameIn(List<String> names); // 일반적으로 다른 쿼리의 결과값을 받아 in 절을 처리하는데 쓴다.

    // like
    List<User> findByNameStartingWith(String name);
    List<User> findByNameEndingWith(String name);
    List<User> findByNameContains(String name);
    List<User> findByNameLike(String name);

    // limit
    List<User> findTop1ByName(String name);

    // order by
    List<User> findTopByNameOrderByIdDesc(String name);
    List<User> findFirstByNameOrderByIdDescEmailAsc(String name);
    List<User> findFirstByName(String name, Sort sort);

    // paging
    // slice : 현재 묶음(부분집합)에 대한 각각의 정보들
    // page : slice + 전체 페이지에대한 정보를 추가 제공
    // -> paging 에 대한 응답값
    // Pageable : paging 에 대한 요청값
    Page<User> findByName(String name, Pageable pageable);

    // 네이티브 쿼리
    // 컬럼명이 key값 해당 쿼리값이 value에 저장된다. (리턴이 가능하면 어떤 타입이든 반환타입이 될 수 있다.)
    @Query(value = "select * from user limit 1;", nativeQuery = true)
    Map<String, Object> findRawRecord();

    @Query(value = "select * from user", nativeQuery = true)
    List<Map<String, Object>> findAllRawRecord();
}
