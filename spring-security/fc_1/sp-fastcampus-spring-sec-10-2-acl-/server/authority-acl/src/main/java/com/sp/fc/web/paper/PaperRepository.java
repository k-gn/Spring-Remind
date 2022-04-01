package com.sp.fc.web.paper;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaperRepository extends JpaRepository<Paper, Long> {

    // 스프링은 기본적으로 aop 로 캐시를 쓴다.
//    @Cacheable(value="papers") // id가 key 값이고 이름이 papers 인 캐시가 만들어지고 캐시로 관리된다.
    Optional<Paper> findById(Long id);

}
