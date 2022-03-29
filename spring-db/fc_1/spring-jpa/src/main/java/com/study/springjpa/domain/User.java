package com.study.springjpa.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor // jpa에서 필수
@AllArgsConstructor
@RequiredArgsConstructor // NonNull or final, 다른 consturctor 어노테이션 존재 시 @Data 있어도 작성해줘야함
@Data
// callSuper: 자동 생성 시 부모 클래스의 필드까지 감안할지 안 할지에 대해서 설정
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder // 빌더 형식으로 객체를 생성하고 필드값을 주입해준다.
@Entity // 자바 객체를 엔티티로 선언, PK가 반드시 필요하다.
// @Table 엔티티와 매핑할 테이블을 지정, 테이블명, 인덱스, 제약조건 등을 지정해줄 수 있다.
// @Table(name = "user", indexes = { @Index(columnList = "name")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User extends BaseEntity {

    @Id // PK 설정
    // identity : mysql에서 일반적으로 많이 쓴다. (auto increment)
    // sequence : oracle에서 일반적으로 많이 쓴다. (or postgre)
    // table : db 종류에 상관없이 별도에 table을 만들어 사용
    // auto : default (db에 적합한 값을 자동으로 설정)
    /*
        IDENTITY : 데이터베이스에 위임(MYSQL)
            Auto_Increment
        SEQUENCE : 데이터베이스 시퀀스 오브젝트 사용(ORACLE)
            @SequenceGenerator 필요
        TABLE : 키 생성용 테이블 사용, 모든 DB에서 사용
            @TableGenerator 필요
        AUTO : 방언에 따라 자동 지정, 기본값
    */
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가값
    private Long id;

    @NonNull
    private String name;

    @NonNull
//    @Column(unique = true)
    private String email;

//    @Column(columnDefinition = "integer default 25")
//    private int age;

    // 알아서 0 이나 1 등(ordinal)으로 다룰 경우 객체 매핑 및 출력 시 해당 문자로 컨버팅 되지만,
    // ordinal 방식은 추 후 enum이 추가되거나 수정될 때 문제가 발생할 수 있다.
    // 따라서 반드시 EnumType.String 으로 설정하여 문자로 다루자.
    // Enum 설정 어노테이션
    // EnumType.ORDINAL : enum 순서 값을 DB에 저장
    // EnumType.STRING : enum 이름을 DB에 저장
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    // Many 쪽이 FK 키를 가지고 있다고 생각하자.
    @OneToMany(fetch = FetchType.EAGER)
    // @JoinColumn : 조인할 컬럼 지정, OneToMany 시 필수 (중간 연결테이블을 만들지 않음)
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // User 에서 UserHistory 를 히스토리 값 특성상 readOnly 로 설정
    @ToString.Exclude
    private List<UserHistory> userHistories = new ArrayList<>();

    @OneToMany // 사실 일대다 양방향 매핑은 존재하지 않음
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    // AttributeOverrides 로 재정의할지 새로운 객체를 만들지는 개발자 취향
    @Embedded
    @AttributeOverrides({ // 동일한 컬럼을 재정의 할 수 있는 어노테이션 (임베디드 타입에 정의한 매핑정보를 재정의)
            @AttributeOverride(name = "city", column = @Column(name = "home_city")),
            @AttributeOverride(name = "district", column = @Column(name = "home_district")),
            @AttributeOverride(name = "detail", column = @Column(name = "home_address_detail")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code"))
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "company_city")),
            @AttributeOverride(name = "district", column = @Column(name = "company_district")),
            @AttributeOverride(name = "detail", column = @Column(name = "company_address_detail")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "company_zip_code"))
    })
    private Address companyAddress;


    // nullable 같은 것들은 사전에 validation을 해주는게 아니라 db 컬럼의 not null 속성 같은 것을 지정해주는 것이다.
//    @Column(name = "crtdat", nullable = false, updatable = false, unique = false) // 별도로 컬럼 매핑 및 속성 설정
//    @CreatedDate
//    private LocalDateTime createdAt;

//    @Column(insertable = false)
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

//    @Transient : 영속성 처리에서 제외 -> db에 반영되지 않는다.
//    db에선 처리하지 않지만 객체에서 따로 쓸 필드가 존재한다면 활용가능
//    private String testdata;

    // Listener : 이벤트를 관찰하다가 발생 시 특정한 동작을 진행
//    @PrePersist // insert 전
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate // update 전
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
}
