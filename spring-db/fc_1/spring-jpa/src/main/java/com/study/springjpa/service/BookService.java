package com.study.springjpa.service;

import com.study.springjpa.domain.Book;
import com.study.springjpa.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional // 클래스에도 선언 가능 - 모든 메소드에 트랜잭션 적용
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AuthorService authorService;

    // 2
    public void putTest() throws Exception {
        this.put();
    }

    // 쓰기지연으로 실제 쿼리를 commit 하지 않고 있다가 트랜잭션 완료 시점에 쿼리를 보낸다. (에러 발생 시 롤백)
    // 1
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED) // 무분별하게 전파설정 시 복잡해진다. (제대로 설계해야한다)
    public void put() throws Exception {
        Book book = new Book();
        book.setName("JPA 시작하기");
        bookRepository.save(book);
        authorService.put(); //  try catch 로 감싸도 롤백은 된다.

        throw new Exception("error!!");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void get(Long id) {
        System.out.println(" >>> " + bookRepository.findById(id));
        System.out.println(" >>> " + bookRepository.findAll());

        // 엔티티 캐시로 인해 db에 커밋된 데이터가 아닌 영속성 컨텍스트 캐시에서 가져올 수 있다. 따라서 db에서 가져오려면 캐시를 비워야 한다.
        entityManager.clear();
        System.out.println("===============================================");

        // 단지 조회를 했을 뿐인데 값이 변경되어 있는 현상 : unRepeatable read 현상 (조작은 하지 않았으나 트랜잭션 내에서 조회값이 달라지는 현상)
        System.out.println(" >>> " + bookRepository.findById(id));
        System.out.println(" >>> " + bookRepository.findAll());

        // 트랜잭션 내에서 한개의 레코드를 확인 후 한개만 수정될 거라 예상했지만
        // 내부 트랜잭션에서 insert 한 내용도 같이 수정됨 => 팬텀 리드 (실제로 두개의 레코드가 변경된 것 - 데이터가 안보이는데 처리됨)
        bookRepository.update();

        entityManager.clear();

        // 트랜잭션 내에서 다른 트랜잭션 동작 시
        // 바깥 트랜잭션은 내부 트랜잭션 동작 종료까지 락이 걸린다.

        // 데이터 수정 시 내부 트랜잭션에서 롤백시켜도 READ_UNCOMMITTED 라면 업데이터 쿼리에 의해 데이터가 수정되어 문제가 발생할 수 있다.
        // 따라서 @DynamicUpdate 를 사용해 불필요한 업데이트 컬럼을 제거
//        Book book = bookRepository.findById(id).get();
//        book.setName("바뀔까?");
//        bookRepository.save(book);

    }

    @Transactional
    public void getAll() {
        List<Book> books = bookRepository.findAll();
        System.out.println("=================================================");
        books.forEach(System.out::println);

        System.out.println("=================================================");
//        return books;
    }


}
// 트랜잭션의 잘못된 사용
// 1. 런타임이 아닌 checkedException(Exception - RuntimeException을 상속하지 않는 클래스) 을 사용할 때 롤백 처리안됨
// 즉 RuntimeException 또는 Error 일 경우에만 롤백이 된다.
// checkedException 일 때에도 롤백을 허용하고 싶다면 rollbackFor 속성을 사용한다.

// 2. private 메서드에서 사용할 경우 또는 같은 클래스내에서 호출한 경우 트랜잭션 효과가 무시된다.

