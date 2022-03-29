package com.study.springjpa.repository;

import com.study.springjpa.domain.Book;
import com.study.springjpa.repository.dto.BookNameAndCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 커스텀 쿼리
    @Modifying // 어노테이션으로 작성된 수정, 삭제 쿼리 메소드를 사용(조회 쿼리를 제외하고 데이터에 변경)할 때 필요
    @Query(value = "update book set category = 'none'", nativeQuery = true)
    void update();

    List<Book> findByCategoryIsNull();

    List<Book> findByDeletedFalse();

    List<Book> findByCategoryIsNullAndDeletedFalse();

    // 가독성 최악
    List<Book> findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(String name, LocalDateTime createdAt, LocalDateTime updatedAt);

    // @Query 어노테이션을 이용한 쿼리 직접 정의
    // 보통 간단한 쿼리는 메소드 이름으로 만들고 아니면 @Query 메소드나 QueryDsl을 사용한다.
    // 기본타입 하나만 있을 경우 기본 타입으로 지정할 수 있다.
    // 현재 필요한 데이터만을 Object[] 형태로 추출할수 있다
    // 애플리케이션 실행 시점에 문법 오류를 발견 할 수 있는 장점이 있다.
    // jpql 로 작성 (native true 시 sql로 작성 가능)
    // jpql은 방언에 따라 자동으로 해당 db에 맞는 쿼리가 생성된다.
    @Query(value = "select b from Book b "
            + "where b.name = :name and b.createdAt >= :createdAt and b.updatedAt >= :updatedAt and b.category is null")
    List<Book> findByNameRecently(
            @Param("name") String name, // @Param 으로 원하는 이름으로 지정 가능
            LocalDateTime createdAt, // 생략가능 (단, sql에 매핑 시 해당 변수명과 동일해야함)
            @Param("updatedAt") LocalDateTime updatedAt);

    // 기본적으로 원하는 컬럼만 뽑았을 경우(프로젝션) 리턴 타입은 Tuple
    // 프로젝션 대상이 둘 이상이라면 Tuple 타입을 반환 (Projection 기능은 엔티티의 일부 데이터만을 가져오게 하는 기능)
//    @Query(value = "select b.name as name, b.category as category from Book b")
//    List<Tuple> findBookNameAndCategory();

    // Object[]
//    @Query(value = "select b.name as name, b.category as category from Book b")
//    List<Object[]> findBookNameAndCategoryObj();

    @Query(value = "select b.id from Book b")
    Set<Long> findId();

    // 다른 객체로 직접 매핑 해주려면 new 명령어를 사용해야 하고, 패키지경로를 모두 작성해줘야 한다.
    @Query(value = "select new com.study.springjpa.repository.dto.BookNameAndCategory(b.name, b.category) from Book b")
    List<BookNameAndCategory> findBookNameAndCategory();

    // 페이징 처리
    @Query(value = "select new com.study.springjpa.repository.dto.BookNameAndCategory(b.name, b.category) from Book b")
    Page<BookNameAndCategory> findBookNameAndCategory(Pageable pageable);

    // nativeQuery
    // 엔티티 속성은 사용할 수 없다.
    // 말 그대로 db에 sql을 그대로 사용한다.
    // 방언이 자동으로 적용되지 않는다.
    // 다른 모든 설정들을 제외하고 딱 value에 지정한 쿼리가 실행된다.
    // 현업에서는 db를 쓰다가 변경하는 경우가 별로 없다. (단. 기존 db와 테스트용으로 h2 db를 이종으로 쓰는 경우는 있다 -> 이 때 오작동 가능성이 있음)
    // nativeQuery 는 최소한으로 사용하는게 좋다.
    @Query(value = "select * from book", nativeQuery = true)
    List<Book> findAllCustom();

    // 1. 성능에 대한 문제를 해결하는데 활용할 수 있다. ex) deleteAllInBatch 처럼 update 문도 한번에 처리해주기
    @Transactional // SELECT를 제외한 DML 직접 작성 시 필수 (스프링에선 인터페이스 보단 구체 클래스나 사용처에서 쓰는걸 권장하나 뭐 취향이나 상황에 따라 인듯)
    @Modifying // SELECT를 제외한 DML 직접 작성 시 필수
    @Query(value = "update book set category = 'IT전문서'", nativeQuery = true)
    int updateCategories(); // 리턴되는 값은 업데이트 되는 row 수

    // 2. jpa 에서 기본적으로 지원하지 않는 경우 사용
    @Query(value = "show tables", nativeQuery = true)
    List<String> showTables();

    // 저장한 값을 map 형식으로 한개 받아오기
    @Query(value = "select * from book order by id desc limit 1", nativeQuery = true)
    Map<String, Object> findRawRecord();
}