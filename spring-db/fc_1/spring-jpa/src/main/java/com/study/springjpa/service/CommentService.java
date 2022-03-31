package com.study.springjpa.service;

import java.util.List;

import com.study.springjpa.domain.Comment;
import com.study.springjpa.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin
 * @since 2021/06/16
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public void init() {
        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setComment("최고예요");

            commentRepository.save(comment);
        }
    }

    // 더티 체크 : 영속성 컨텍스트에서 관리중에 일어난 변경은 별도의 save 호출이 없어도 DB에 영속화 시켜준다.
    // SELECT를 한 엔티티에 대해서 하나하나 더티체크를 하는 과정으로 인한 성능 이슈가 있다.
    @Transactional(readOnly = true) // readOnly = true : 더티 체크 X(flush 사용 x), 읽기 전용으로 조회만 가능 (대용량 조회속도 개선)
    public void updateSomething() {
        List<Comment> comments = commentRepository.findAll();

        for (Comment comment : comments) {
            comment.setComment("별로예요");

//            commentRepository.save(comment);
        }
    }

    @Transactional
    public void insertSomething() {
//        Comment comment = new Comment(); // 새로 생성된 애는 영속화 되어있지 않아서 더티체크가 발생하지 않음
        Comment comment = commentRepository.findById(1L).get();
        comment.setComment("이건뭐죠?");

//        commentRepository.save(comment);
    }
}
