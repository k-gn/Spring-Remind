package com.study.springjpa.repository;

import com.study.springjpa.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort.Order;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // spring context loading
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
//    @Transactional // lazy 로딩 때 세션 유지 + test code에서 사용 시 자동으로 rollback
    void crudTest() {
        userRepository.save(new User());
//        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name")); // 정렬
//        List<User> users = userRepository.findAllById(Lists.newArrayList(1L, 3L, 5L)); // in
//        List<User> users = userRepository.findAll();
//        users.forEach(System.out::println);
//        System.out.println("=========================================");

//        User user4 = userRepository.getOne(1L); // getOne : lazy (reference 만 가지고 있다가 실제 조회 시 proxy 를 통해 가져옴)
//        System.out.println(user4);

//        User user = userRepository.findById(4L).orElse(null); // findById : eager
//        System.out.println(user);

//        userRepository.saveAll(List.of(new User("new martin1", "newmartin1@nm.com"), new User("new martin2", "newmartin2@nm.com"));
//        userRepository.save(new User("new martin", "newmartin@nm.com"));
//        userRepository.flush(); // db 반영시점을 조절 (영속성 컨텍스트의 변경 내용을 DB 에 반영)

//        long count = userRepository.count();
//        System.out.println(count);
//        boolean exists = userRepository.existsById(1L);
//        System.out.println(exists);

//        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));
//        userRepository.deleteById(1L);
//        userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(1L, 3L)));

        // ~All 메소드는 반복을 돌기 때문에 데이터가 많을 수록 성능이 안좋다.
        // ~InBatch 메소드는 반복을 돌지 않고 or 연산을(한번에 수행) 사용한다.
//        userRepository.deleteInBatch(userRepository.findAllById(Lists.newArrayList(1L, 3L)));
//        userRepository.deleteAllInBatch();

        // 페이징 처리
//        Page<User> users = userRepository.findAll(PageRequest.of(1, 3));
//        System.out.println(users);
//        System.out.println(users.getTotalElements()); // 요소의 총 개수
//        System.out.println(users.getTotalPages()); // 총 페이지 수
//        System.out.println(users.getNumberOfElements()); // 현재 가져온 레코드 수 (페이지는 0부터 시작)
//        System.out.println(users.getSort()); // 정렬 정보
//        System.out.println(users.getSize()); // 페이징 할때 나누는 크기
//        users.getContent().forEach(System.out::println); // 내부 user 정보 가져오기

        // 검색이 필요한 인자를 Example 이란 놈을 사용해 쿼리를 만들어 보낼 수 있다.
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withIgnorePaths("name") // name 은 매칭하지 않음
//                .withMatcher("email", endsWith()); // email 은 매칭 (like endsWith)

//        Example<User> example = Example.of(new User("ma", "fastcampus.com"), matcher); // name = "ma" and email like "fastcampus.com"
//        userRepository.findAll(example).forEach(System.out::println);

//        User user = new User();
//        user.setEmail("slow");
//        System.out.println(">>> " + userRepository.findAll());
//        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("email", contains()); // email like ?
//        Example<User> example = Example.of(user, matcher);
//        userRepository.findAll(example).forEach(System.out::println);

        // update (해당 엔티티가 존재하는지 select 를 수행한다)
        userRepository.save(new User("david", "david@david.com"));
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setEmail("martin-update@fc.com");
        userRepository.save(user);
        // SimpleJpaRepository
    }

    @Test
    void select() {
        System.out.println(userRepository.findByName("dennis"));
        System.out.println("getByEmail : " + userRepository.getByEmail("martin@fastcampus.com"));
        System.out.println("readByEmail : " + userRepository.readByEmail("martin@fastcampus.com"));
        System.out.println("queryByEmail : " + userRepository.queryByEmail("martin@fastcampus.com"));
        System.out.println("searchByEmail : " + userRepository.searchByEmail("martin@fastcampus.com"));
        System.out.println("streamByEmail : " + userRepository.streamByEmail("martin@fastcampus.com"));
        System.out.println("findUserByEmail : " + userRepository.findUserByEmail("martin@fastcampus.com"));

        System.out.println("findSomethingByEmail : " + userRepository.findSomethingByEmail("martin@fastcampus.com"));

        System.out.println("findTop2ByName : " + userRepository.findTop2ByName("martin"));
        System.out.println("findFirst2ByName : " + userRepository.findFirst2ByName("martin"));
        System.out.println("findLast1ByName : " + userRepository.findLast1ByName("martin"));

        System.out.println("findByEmailAndName : " + userRepository.findByEmailAndName("martin@fastcampus.com", "martin"));
        System.out.println("findByEmailOrName : " + userRepository.findByEmailOrName("martin@fastcampus.com", "dennis"));

        System.out.println("findSomethingByEmail : " + userRepository.findSomethingByEmail("martin@fastcampus.com"));
        System.out.println("findByCreatedAtAfter : " + userRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(1L))); //minusDays : 현재날짜 - 1
        System.out.println("findByIdAfter : " + userRepository.findByIdAfter(4L));
        System.out.println("findByCreatedAtGreaterThan : " + userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByCreatedAtGreaterThanEqual : " + userRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(1L)));

        System.out.println("findTop2ByName : " + userRepository.findTop2ByName("martin"));
        System.out.println("findFirst2ByName : " + userRepository.findFirst2ByName("martin"));
        System.out.println("findLast1ByName : " + userRepository.findLast1ByName("martin"));
        System.out.println("findByCreatedAtBetween : " + userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween : " + userRepository.findByIdBetween(1L, 3L));
        System.out.println("findByIdGreaterThanEqualAndIdLessThanEqual : " + userRepository.findByIdGreaterThanEqualAndIdLessThanEqual(1L, 3L));

        System.out.println("findByIdIsNotNull : " + userRepository.findByIdIsNotNull());
//        System.out.println("findByIdIsNotEmpty : " + userRepository.findByAddressIsNotEmpty());

        System.out.println("findByNameIn : " + userRepository.findByNameIn(Lists.newArrayList("martin", "dennis")));

        System.out.println("findByNameStartingWith : " + userRepository.findByNameStartingWith("mar"));
        System.out.println("findByNameEndingWith : " + userRepository.findByNameEndingWith("tin"));
        System.out.println("findByNameContains : " + userRepository.findByNameContains("art"));

        System.out.println("findByNameLike : " + userRepository.findByNameLike("%" + "art" + "%")); // % 직접 줘야함
    }

    @Test
    void pagingAndSortingTest() {
//        System.out.println("findTop1ByName : " + userRepository.findTop1ByName("martin"));
//        System.out.println("findTopByNameOrderByIdDesc : " + userRepository.findTopByNameOrderByIdDesc("martin"));
//        System.out.println("findFirstByNameOrderByIdDescEmailAsc : " + userRepository.findFirstByNameOrderByIdDescEmailAsc("martin"));
//        System.out.println("findFirstByNameWithSortParams : " + userRepository.findFirstByName("martin", Sort.by(Order.desc("id"), Order.asc("email"))));
        System.out.println("findByNameWithPaging : " + userRepository.findByName("martin", PageRequest.of(1, 1, Sort.by(Order.desc("id")))).getTotalElements());
    }
}