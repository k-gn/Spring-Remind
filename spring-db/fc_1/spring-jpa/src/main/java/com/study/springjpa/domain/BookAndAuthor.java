package com.study.springjpa.domain;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Martin
 * @since 2021/04/21
 */
@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BookAndAuthor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 실무에선 ManyToMany 를 안쓴다.
    // ManyToMany -> OneToMany 와 ManyToOne 으로 풀어서 설계

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @ManyToOne
    private Author author;
}