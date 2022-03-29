package com.study.springjpa.repository;

import com.study.springjpa.domain.Address;
import com.study.springjpa.domain.Gender;
import com.study.springjpa.domain.User;
import com.study.springjpa.domain.UserHistory;
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

import static org.assertj.core.api.Assertions.assertThat;
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

        System.out.println("findByNameStartingWith : " + userRepository.findByNameStartingWith("mar")); // ~ %
        System.out.println("findByNameEndingWith : " + userRepository.findByNameEndingWith("tin")); // % ~
        System.out.println("findByNameContains : " + userRepository.findByNameContains("art")); // % ~ %

        System.out.println("findByNameLike : " + userRepository.findByNameLike("%" + "art" + "%")); // % 직접 줘야함
    }

    @Test
    void pagingAndSortingTest() {
        System.out.println("findTop1ByName : " + userRepository.findTop1ByName("martin"));
        System.out.println("findTopByNameOrderByIdDesc : " + userRepository.findTopByNameOrderByIdDesc("martin"));
        System.out.println("findFirstByNameOrderByIdDescEmailAsc : " + userRepository.findFirstByNameOrderByIdDescEmailAsc("martin"));
        System.out.println("findFirstByNameWithSortParams : " + userRepository.findFirstByName("martin", Sort.by(Order.desc("id"), Order.asc("email"))));
        System.out.println("findByNameWithPaging : " + userRepository.findByName("martin", PageRequest.of(1, 1, Sort.by(Order.desc("id")))).getTotalElements());
    }

    @Test
    void insertAndUpdateTest() {
        User user = new User();
        user.setName("martin");
        user.setEmail("martin2@fastcampus.com");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("marrrrrtin");

        userRepository.save(user2);
    }

    @Test
    void enumTest() {
//        User user1 = new User("jack", "jack@jack.com");
//        User user2 = new User("steve", "steve@steve.com");
//        User user3 = new User("john", "john@john.com");
//        userRepository.saveAll(Lists.newArrayList(user1, user2, user3));

        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setGender(Gender.MALE);

        userRepository.save(user);

        // gender 출력 시 ordinal 이여도 자동으로 string 으로 출력됨 ==> jpa 에서 enum 데이터를 가져올 때 Converter 가 동작한다.
        // 자바 객체화 시 db 데이터와 형식이 다를 경우 Converter 를 통해서 데이터를 가져오는 즉시 정보를 변경해서 핸들링 가능
        userRepository.findAll().forEach(System.out::println);

        System.out.println(userRepository.findRawRecord().get("gender"));
//        System.out.println(userRepository.findRawRecord().get("email"));
    }

    @Test
    void listenerTest() {
        User user = new User();
        user.setEmail("martin2@fastcampus.com");
        user.setName("martin");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("marrrrrtin");

        userRepository.save(user2);

        userRepository.deleteById(4L);
    }

    @Test
    void prePersistTest() {
        User user = new User();
        user.setEmail("martin2@fastcampus.com");
        user.setName("martin");
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        System.out.println(userRepository.findByEmail("martin2@fastcampus.com"));
    }

    @Test
    void preUpdateTest() {
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);

        System.out.println("as-is : " + user);

        user.setName("martin22");
        userRepository.save(user);

        System.out.println("to-be : " + user);
        System.out.println("to-be : " + userRepository.findAll().get(0));
    }

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Test
    void userHistoryTest() {
        User user = new User();
        user.setEmail("martin-new@fastcampus.com");
        user.setName("martin-new");

        userRepository.save(user);

        user.setName("martin-new-new");

        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
    }

    @Test
    void userRelationTest() {
        User user = new User();
        user.setName("david");
        user.setEmail("david@fastcampus.com");
        user.setGender(Gender.MALE);
        userRepository.save(user); // insert

        user.setName("daniel");
        userRepository.save(user); // update

        user.setEmail("daniel@fastcampus.com");
        userRepository.save(user);

//        userHistoryRepository.findAll().forEach(System.out::println);

//        List<UserHistory> result = userHistoryRepository.findByUserId(
//            userRepository.findByEmail("daniel@fastcampus.com").getId());

        List<UserHistory> result = userRepository.findByEmail("daniel@fastcampus.com").getUserHistories();

        result.forEach(System.out::println);

        System.out.println("UserHistory.getUser() : " + userHistoryRepository.findAll().get(0).getUser());
    }

    @Test
    void embedTest() {
//        userRepository.findAll().forEach(System.out::println);
//        System.out.println("=========================================");
        User user = new User();
        user.setName("steve");
        user.setHomeAddress(new Address("서울시", "강남구", "강남대로 364 미왕빌딩", "06241"));
        user.setCompanyAddress(new Address("서울시", "성동구", "성수이로 113 제강빌딩", "04794"));

        userRepository.save(user);
//        userRepository.findAll().forEach(System.out::println);
//        userHistoryRepository.findAll().forEach(System.out::println);

        User user1 = new User();
        user1.setName("joshua");
        user1.setHomeAddress(null);
        user1.setCompanyAddress(null);

        userRepository.save(user1);

        User user2 = new User();
        user2.setName("jordan");
        user2.setHomeAddress(new Address());
        user2.setCompanyAddress(new Address());

        userRepository.save(user2);

        // address 값이 그냥 null 로 들어가거나 Address(null, null, null, null) 로 들어가는 두가지 경우가 있다.
        // 그 이유는 영속성 캐시 때문에,, (실제 db 값은 똑같이 null)
//        entityManager.clear();

        userRepository.findAll().forEach(System.out::println);
        userHistoryRepository.findAll().forEach(System.out::println);

        userRepository.findAllRawRecord().forEach(a -> System.out.println(a.values()));

        assertAll(
                () -> assertThat(userRepository.findById(7L).get().getHomeAddress()).isNull(),
                () -> assertThat(userRepository.findById(8L).get().getHomeAddress()).isInstanceOf(Address.class)
        );
    }
}