package com.study.springjpa.service;

import com.study.springjpa.domain.User;
import com.study.springjpa.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
// 영속화 : 사라지지 않고 지속적으로 접근할 수 있다는 의미.
// 보통 메모리는 서비스 종료 시 사라진다.
// 데이터를 영속화하는데 사용하는 컨테이너 : 영속성 컨텍스트
// 엔티티 매니저 빈이 가장 주체가 된다.
// persistence.xml 을 사용해서 영속성 컨텍스트 설정을 할 수 있다.
// 부트에선 스프링 데이터 JPA 를 추가하면 스프링이 자동으로 영속성 설정을 해준다.

// ## 더 자세한 내용은 즐겨찾기 해놓은 블로그들 참고하면 이해 잘됨

// 엔티티 매니저는 캐시를 사용한다.
// 조회한 앤티티가 캐시에 이미 있으면 select 조회하지 않고 이미 존재하는 엔티티를 사용한다. => JPA의 1차캐시 (map 형태 - id,entity)
// 하이버네이트에서 앤티티 매니저는 세션이라고도 부른다.

@SpringBootTest
@Transactional
public class EntityManagerTest {

    // 만약 스프링 데이터 jpa 에서 제공하지 않는 기능을 써야 되거나
    // 성능적인 이슈 등 특별한 문제가 발생해 별도로 커스터마이징 해야 한다면
    // 직접 엔티티 매니저를 주입받아 사용할 수 있다.
    @Autowired
    private EntityManager entityManager; // SessionImpl 클래스가 구현체

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void before() {
        User user1 = new User("martin", "martin@fastcam.com");
        User user = userRepository.save(user1);
    }

    @Test
    public void test() {
        // jpql 사용
        System.out.println(entityManager.createQuery("select u from User u").getResultList()); // == findAll
        // 기존에 사용하던 SimpleJpaRepository 는 직접적으로 EntityManager 를 사용하지 않도록 한번 더 래핑해서 쉽게 사용할 수 있도록 제공한 구현 클래스
        // SimpleJpaRepository 나 쿼리메소드나 모두 내부적으로 EntityManager 를 통해서 실행된다.
    }

    @Test
    public void cacheTest() {

//        System.out.println(userRepository.findById(1L).get());
//        System.out.println(userRepository.findById(1L).get());
//        System.out.println(userRepository.findById(1L).get());
        System.out.println(userRepository.findByEmail("martin@fastcam.com"));
        System.out.println(userRepository.findByEmail("martin@fastcam.com"));
//
        userRepository.deleteById(1L);

    }

    @Test
    public void cacheTest2() {
        User user = userRepository.findById(1L).get();
        user.setName("marrrrrrrtin");

        // 트랜잭션이 없으면 각각 메소드 내부에 트랜잭션이 있어서 각각이 하나의 트랜잭션이 된다.
        // 지정해주면 전체가 하나의 트랜잭션이 된다.
        userRepository.save(user);

        System.out.println("===============================");

        user.setName("martinsssss");
        userRepository.save(user);
        System.out.println("===============================");

//        userRepository.flush(); // 해당 시점에 DB에 적용 (개발자가 원하는 시점에 반영 가능하나 남발하면 안된다)
        // 보면 update 쿼리가 한번만 실행되는걸 볼 수 있다.
        // 영속성 컨텍스트 내에서 저장해두고(캐시에 merge) 최종적으로 한번만 DB에 전송한다. -> 쓰기 지연 SQL 저장소
        // 변경 감지 (Dirty Checking)
        // 트랜잭션을 커밋하게 되면, flush() 와 commit() 두가지 일을 하는 것

        // flush 호출, 트랜잭션 완료 후 커밋(test에선 기본적으로 롤백되서 제외), 복잡한 조회조건의 jpql 쿼리 실행 시 -> 영속성 컨텍스트와 DB가 서로 동기화 된다.

//        System.out.println("1 : " + userRepository.findById(1L).get()); // 이땐 아직 db 반영 x
//        userRepository.flush();
//        System.out.println("2 : " + userRepository.findById(1L).get());

        // 일반적으론 조회 시에 영속성 캐시와 db 데이터를 비교해서 최신값을 사용해야 한다.
        // 이런 경우 영속성 캐시 값을 DB에 모두 반영 후 Select 해서 다시 가져온다.
        System.out.println(userRepository.findAll());
    }
}

// 엔티티 매니저가 영속성 컨텍스트 내에서 엔티티 상태를 변화시켜준다
// # 엔티티 라이프사이클
// - 비영속(new/transient) : 영속성 컨텍스트와 전혀 관계가 없는 상태
// - 영속(managed) : 영속성 컨텍스트에 저장된 상태, 엔티티가 영속성 컨텍스트에 의해 관리,
//                  이때 DB에 저장 되지 않는다. 영속 상태가 된다고 DB에 쿼리가 날라가지 않는다.
//                  트랜잭션의 커밋 시점에 영속성 컨텍스트에 있는 정보들이 DB에 쿼리로 날라간다.
// - 준영속(detached) : 영속성 컨텍스트에 저장되었다가 분리된 상태
// - 삭제(removed) : 삭제된 상태. DB에서도 날린다.