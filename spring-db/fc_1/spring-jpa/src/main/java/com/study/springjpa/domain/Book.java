package com.study.springjpa.domain;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ERD 로 그려보면서 먼저 테이블간의 연관관계 설계를 하면 좋다.

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@EntityListeners(value = AuditingEntityListener.class) // EntityListeners - JPA Entity에서 이벤트가 발생할 때마다 특정 로직을 실행
@DynamicUpdate // 필요한 정보만 업데이트 => 실제 값이 변경된 컬럼으로만 update 쿼리를 만드는 기능
@Where(clause = "deleted = false") // where 절 추가
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String author;

//    private String category;

//    private Long authorId;

//    @Column(name = "publisher_id")
//    private Long publisherId;

    // JPA에서는 두 객체 연관관계 중 하나를 정해서 테이블의 외래키를 관리해야 하는데 이것을 연관관계의 주인이라고 한다.
    // 주인이 아니면 mappedBy 속성을 사용해서 속성의 값으로 연관관계의 주인을 지정
    // 연관관계의 주인은 외래 키가 있는 곳으로 설정
//    @OneToOne(mappedBy = "book") // mappedBy : 양방향 매핑일 때 사용, 반대쪽 매핑의 필드 이름을 값, 해당 테이블에서 연관키가 사라진다.
//    @ToString.Exclude // ToString 에서 제외 : 엔티티 릴레이션에서 순환참조를 없애기
//    private BookReviewInfo bookReviewInfo;

    // One 쪽의 PK 를 Many 쪽에서 FK 로 가지고 있다.
//    @OneToMany
//    @JoinColumn(name = "book_id")
//    @ToString.Exclude
//    private List<Review> reviews = new ArrayList<>();


    // 보통 manytoone 쪽에 joincolumn 쓰고 onetomany 쪽에 mappedby 쓰는 것 같다.. (mappedby 도 중간테이블 안만들어줌)
    // 연관관계가 있는 경우 cascade 설정 가능 (Entity의 상태 변화를 전파시키는 옵션)
    // 예를 들어 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장하거나, 부모 엔티티를 삭제할 때 관련된 자식엔티티도 함께 삭제하고 싶을 때 사용
//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//    private Publisher publisher;

    //    @ManyToMany
//    @OneToMany
//    @JoinColumn(name = "book_id")
//    @ToString.Exclude
//    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();
//
//    public void addBookAndAuthors(BookAndAuthor... bookAndAuthors) {
//        Collections.addAll(this.bookAndAuthors, bookAndAuthors);
//    }

    // flag를 활용한 soft delete 방식
    // 데이터 삭제 시 직접 delete 쿼리로 삭제하는 경우가 현업에선 거의 없다.
    // flag 변수를 하나 두고 처리하는 경우가 많다.
    // db 상에는 존재하지만 숨겨서 가져오는 방식
    private boolean deleted;

//    private int status; // 판매 상태
//    @Convert(converter = BookStatusConverter.class) // @Convert : 해당 필드에 적용할 컨버터 클래스 작성
//    private BookStatus status; // 판매상태

//    public boolean isDisplayed() {
//        return status == 200;
//    }

//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
}

// 백기선 문제 설명
// JPA 에서 @OneToMany 애노테이션을 사용하면 mappedBy 속성을 지정해주게된다. 상대편의 어떤 엔티티에 의해 매핑될것인지를 명시해주는것이다.
// 그리고 1:N 관계에서 연관관계의 주인은 N이 되게된다. 때문에 mappedBy 를 이용한 필드는 JPA 에서 read-only 로 이용되어 실제 insert 시에는 insert 되지않는다.
// 객체관계에서는 양방향 참조를 위해서는 서로가 서로를 참조로 갖고있어야한다.
// RDB 관계에서 외래키는 N 이 보유하게된다. 즉 bookStore 가 book 을 갖고있는게 아니라 book 이 bookStore 를 들고있어야한다.

// cascade 종류 (기본값은 아무것도 없음, 주의해서 사용해야함)
//-> ALL : 모두 적용
//-> PERSIST : 영속 (딱 저장할 때만 life cycle을 맞춰야 하는 경우)
//-> REMOVE : 삭제
//-> MERGE : 병합
//-> REFRESH : REFRESH (다시 로딩)
//-> DETACH : DETACH (영속성 분리)

// 고아속성 제거 (OrphanRemoval)
// : 연관관계가 없는 엔티티를 제거