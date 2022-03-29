package com.study.springjpa.repository;

import com.study.springjpa.domain.Book;
import com.study.springjpa.domain.BookReviewInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReviewInfoRepositoryTest {

    @Autowired
    private BookReviewInfoRepository bookReviewInfoRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void crudTest() {
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
//        bookReviewInfo.setBookId(1L);
        bookReviewInfo.setBook(givenBook());
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);

        System.out.println(bookReviewInfoRepository.findAll());
    }

    @Test
    void crudTest2() {

        givenBookReviewInfo();

        Book result = bookReviewInfoRepository
                .findById(1L)
                .orElseThrow(RuntimeException::new) // Supplier 함수적 인터페이스는 매개값은 없고 리턴값이 있는 getXXX() 메소드를 가지고 있다.
                .getBook();

        System.out.println(">>> " + result);

        // auto increment 는 롤백되도 증가한 상태이다.

        BookReviewInfo result2 = bookRepository
                .findById(1L)
                .orElseThrow(RuntimeException::new)
                .getBookReviewInfo();

        System.out.println(">>> " + result2);
    }

    private Book givenBook() {
        Book book = new Book();
        book.setName("Jpa 초격차 패키지");
//        book.setAuthorId(1L);
//        book.setPublisherId(1L);

        return bookRepository.save(book);
    }

    private void givenBookReviewInfo() {
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
        bookReviewInfo.setBook(givenBook());
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);

//        System.out.println(">>> " + bookReviewInfoRepository.findAll());
    }
}