package com.study.springjpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.study.springjpa.domain.Book;
import com.study.springjpa.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
//    @Autowired
//    private PublisherRepository publisherRepository;
//    @Autowired
//    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private BookService bookService;

    @Test
    void bookTest() {
        Book book = new Book();
        book.setName("Jpa 초격차 패키지");
//        book.setPublisherId(1L);
//        book.setAuthorId(1L);

        bookRepository.save(book);

        System.out.println(bookRepository.findAll());

    }


}
