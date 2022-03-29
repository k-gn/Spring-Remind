package com.study.springjpa.domain.listener;


import com.study.springjpa.domain.User;
import com.study.springjpa.domain.UserHistory;
import com.study.springjpa.repository.UserHistoryRepository;
import com.study.springjpa.support.BeanUtils;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;


public class UserEntityListener {
    // @PostLoad : select 이후 실행
//    @PrePersist
//    @PreUpdate
    @PostPersist
    @PostUpdate
    public void prePersistAndPreUpdate(Object o) { // 감지된 오브젝트를 받음
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class); // 리스너 클래스는 자동 주입을 할 수 없어서 직접 불러와야함

        User user = (User) o;
        System.out.println("user : " + user);

        UserHistory userHistory = new UserHistory();
//        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());
        userHistory.setUser(user); // 연관관계는 참조하고 있어야 동작된다.
//        userHistory.setHomeAddress(user.getHomeAddress());
//        userHistory.setCompanyAddress(user.getCompanyAddress());

        userHistoryRepository.save(userHistory);
        user.getUserHistories().add(userHistory);
        System.out.println("userHistory : " + userHistory);
    }
}
