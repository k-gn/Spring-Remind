package com.study.springjpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.study.springjpa.domain.Book;
import com.study.springjpa.domain.Publisher;
import com.study.springjpa.domain.Review;
import com.study.springjpa.domain.User;
import com.study.springjpa.repository.dto.BookStatus;
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
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private ReviewRepository reviewRepository;
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

    @Test
    @Transactional
    void bookRelationTest() {
        givenBookAndReview();

        User user = userRepository.findByEmail("martin@fastcampus.com");

//        System.out.println("Review : " + user.getReviews());
//        System.out.println("Book : " + user.getReviews().get(0).getBook());
//        System.out.println("Publisher : " + user.getReviews().get(0).getBook().getPublisher());
    }


    @Test
    @Transactional
    public void cascade() {
        Publisher publisher = new Publisher();
        publisher.setName("FastCampus");
//        publisherRepository.save(publisher); // cascade persist

        System.out.println("==================================================1");

        Book book = new Book();
        book.setName("JPA CASCADE");
        book.setPublisher(publisher); // 참조해야 연관관계 설정된다.
        publisher.getBooks().add(book);  // 객체지향적으로 양방향으로 참조하는게 좋다.
        bookRepository.save(book);


        System.out.println("====================================================2");
//        System.out.println("books : " + bookRepository.findAll());
        System.out.println("====================================================3");
//        System.out.println("publishers : " + publisherRepository.findAll());
        System.out.println("====================================================3");

//        Book book1 = bookRepository.findById(1L).get();
//        book1.getPublisher().setName("SLOW");
//        bookRepository.save(book1);
//        // book을 통해서 수정해서 현재 book에 merge cascade가 없으면 수정이 안된다.
//        System.out.println("publishers : " + publisherRepository.findAll());

        System.out.println("====================================================3");

//        Book book2 = bookRepository.findById(1L).get();
//        bookRepository.delete(book2);
//        System.out.println("books : " + bookRepository.findAll());
//        System.out.println("publishers : " + publisherRepository.findAll());

        System.out.println("====================================================3");

        Book book3 = bookRepository.findById(1L).get();
        book3.setPublisher(null); // 연관관계 제거
        bookRepository.save(book3);

        System.out.println("books : " + bookRepository.findAll());
        System.out.println("publishers : " + publisherRepository.findAll());
    }

    @Test
    @Transactional
    void remove() {

        Publisher publisher = new Publisher();
        publisher.setName("FastCampus");
//        publisherRepository.save(publisher); // cascade persist

        System.out.println("==================================================1");

        Book book = new Book();
        book.setName("JPA CASCADE");
        book.setPublisher(publisher);
        publisher.getBooks().add(book);
        bookRepository.save(book);

//        Book book2 = new Book();
//        book2.setName("JPA CASCADE 22");
//        book2.setPublisher(publisher);
//        publisher.getBooks().add(book2);
//        bookRepository.save(book2);

        bookRepository.deleteById(1L);
//        publisherRepository.deleteById(1L);

        System.out.println(bookRepository.findAll());
        System.out.println("====================================================");
        System.out.println(publisherRepository.findAll());
    }

    @Test
    void softDelete() {
        bookRepository.findAll().forEach(System.out::println);
//        bookRepository.findByCategoryIsNull().forEach(System.out::println);
//        bookRepository.findByDeletedFalse().forEach(System.out::println);
    }

    @Test
    void queryTest() {
//        bookRepository.findAll().forEach(System.out::println);

//        System.out.println(bookRepository.findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(
//                "JPA 초격차 패키지",
//                LocalDateTime.now().minusDays(1L),
//                LocalDateTime.now().minusDays(1L)
//        ));

//        System.out.println(bookRepository.findByNameRecently(
//                "JPA 초격차 패키지", LocalDateTime.now().minusDays(1L), LocalDateTime.now().minusDays(1L)));

//        System.out.println(bookRepository.findBookNameAndCategory());
//        bookRepository.findBookNameAndCategory().forEach(tuple -> System.out.println(tuple.get(0)));

//        System.out.println(bookRepository.findId());

//        bookRepository.findBookNameAndCategoryObj().forEach(objs -> {
//            System.out.println(objs[0]);
//            System.out.println("=============================");
//        });

        bookRepository.findBookNameAndCategory(PageRequest.of(0, 2, Sort.by("id").descending())).forEach(System.out::println);
    }

    @Test
    void nativeTest() {
//        bookRepository.findAll().forEach(System.out::println);
//        System.out.println("========================================");
//        bookRepository.findAllCustom().forEach(System.out::println);

//        List<Book> books = bookRepository.findAll();
//        System.out.println("==================================");
//        for(Book book : books) {
//            book.setCategory("IT 전문서");
//        }
//        bookRepository.saveAll(books);
//        System.out.println(bookRepository.findAll());

//        System.out.println("affected rows : " + bookRepository.updateCategories());
//        bookRepository.findAllCustom().forEach(System.out::println);

        System.out.println(bookRepository.showTables());
    }

    @Test
    void converterTest() {
//        bookRepository.findAll().forEach(System.out::println);

        Book book = new Book();
        book.setName("또다른 IT전문서적");
//        book.setStatus(new BookStatus(200));

        bookRepository.save(book);
        System.out.println("============================================");
        System.out.println(bookRepository.findRawRecord().values());
        System.out.println("============================================");

        List<Book> books = bookRepository.findAll();
//        bookRepository.findAll().forEach(System.out::println);
        System.out.println("============================================");
    }

    @Test
    void convert() {
//        bookRepository.findAll();
        System.out.println("========================================");
//        bookService.getAll();
    }


    private void givenBookAndReview() {
        givenReview(givenUser(), givenBook(givenPublisher()));
    }

    private User givenUser() {
        return userRepository.findByEmail("martin@fastcampus.com");
    }

    private void givenReview(User user, Book book) {
        Review review = new Review();
        review.setTitle("내 인생을 바꾼 책");
        review.setContent("너무너무 재미있고 즐거운 책이었어요.");
        review.setScore(5.0f);
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);
    }

    private Book givenBook(Publisher publisher) {
        Book book = new Book();
        book.setName("JPA 초격차 패키지");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private Publisher givenPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("패스트캠퍼스");

        return publisherRepository.save(publisher);
    }
}
