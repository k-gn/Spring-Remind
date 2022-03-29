package com.study.springjpa.repository;

import com.study.springjpa.domain.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void commentTest() {
        Comment comment = new Comment();
        comment.setComment("별로예요");
        comment.setCommentedAt(LocalDateTime.now()); // datetime(6)

        commentRepository.saveAndFlush(comment);

        entityManager.clear();
//
        System.out.println(commentRepository.findById(4L).get()); // datetime

        System.out.println(comment);

        commentRepository.findAll().forEach(System.out::println);

        // 캐시 데이터와 db 데이터 간의 불일치성을 잘 관리해야한다. (중요, 의도치 않은 사이트 이펙트 발생 가능)
    }
}