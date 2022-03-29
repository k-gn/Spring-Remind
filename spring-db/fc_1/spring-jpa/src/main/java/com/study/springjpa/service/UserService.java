package com.study.springjpa.service;

import com.study.springjpa.domain.User;
import com.study.springjpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    public void put() {

        User user = new User(); // 비영속 상태
        user.setName("newUser");
        user.setEmail("new@fcam.com");

        // 영속 상태로 변경 (영속화)
//        userRepository.save(user);
        entityManager.persist(user);

        // 영속성 컨텍스트에서 관리되는 엔티티가 변경될 경우 트랜잭션이 완료된 후 별도로 save 호출이 없어도 알아서 db에 반영된다. => 더티체크
        // 대량의 앤티티를 다룰 경우 성능 저하가 발생할 수도 있다..
//        user.setName("newUserAfterPersist");

        // 준영속 상태로 변경
//        entityManager.detach(user); // == clear, close
        user.setName("newUserAfterPersist");
//        entityManager.merge(user); // 다시 영속상태로 변경하여 반영

//        entityManager.flush(); // clear 전에 flush 를 하는 것을 권장
//        entityManager.clear(); // 다시 준영속 상태로 변경

        // 삭제
        User user1 = userRepository.findById(1L).get();
        entityManager.remove(user1);
    }
}
