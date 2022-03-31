package com.study.springjpa.repository;

import com.study.springjpa.domain.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // N+1 문제 발생 케이스
    // - 즉시 로딩 (fetchType.EAGER) 변경후 findAll()로 조회하는 경우
    // - 지연 로딩(LAZY) 변경 + Loop으로 조회하는 경우
    // n+1 해결방법 (꼭 n+1 이라고 나쁜건 아니다, 오히려 큰 데이터를 한번에 조인으로 가져오는 것이 더 안좋을 수도 있다. 따라서 상황에 맞게 사용,,!)

    // 1. @Query -> fetch join
    @Query("select distinct r from Review r join fetch r.comments") // review 와 comment join jpql,
    // 그냥 join fetch 시 cross join 되서 review가 중복되서 3개로 나온다. => distinct로 해결
    List<Review> findAllByFetchJoin();

    // 2. @EntityGraph : 기존 엔티티의 패치전략과 상관없이 한번에 해당 엔티티를 패치 조인으로 같이 가져온다.
    // 데이터베이스에서 엔티티 및 연관된 엔티티를 로드할 때 성능이 향상되고 JOIN 전략을 사용하여 그래프의 정의된 방식으로만 데이터를 검색하기 때문에
    // 데이터베이스의 모든 정보를 한 번에 조회할 수 있다.
    // JPA가 어떤 엔티티를 불러올 때 이 엔티티와 관계된 엔티티를 불러올 것인지에 대한 정보를 제공
    // 기본적으로는 FECTH 정책을 사용하고 있으며 이것은 설정한 엔티티 속성에는 EAGER 패치 나머지는 LAZY 패치를 하는 정책.
    // 이와 반대로 LOAD 정책은 설정한 엔티티 애트리뷰트는 EAGER 패치 나머지는 기본 패치 전략을 따른다.
    @EntityGraph(attributePaths = "comments", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select r from Review r")
    List<Review> findAllByEntityGraph();

    // 쿼리 메소드에도 적용 가능
//    @EntityGraph(attributePaths = "comments")
//    List<Review> findAll();
}
