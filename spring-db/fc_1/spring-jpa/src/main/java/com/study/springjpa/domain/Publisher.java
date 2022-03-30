package com.study.springjpa.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Publisher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 중간 테이블을 만들지 않기 위해 JoinColumn 을 해준다.
    @OneToMany(orphanRemoval = false) // true : 연관관계가 끊어질 경우 제거
    @JoinColumn(name = "publisher_id") // many 쪽에 name 이름의 FK 가 자동으로 생성된다.
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

}
