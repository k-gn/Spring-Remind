package com.study.springjpa.domain;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * @author Martin
 * @since 2021/06/16
 */
@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
// 영향받는 애들로만 쿼리를 짜게 해준다.
@DynamicInsert // 실제 insert 값만 insert 문에 넣어 실행 (null 이 아닌 값 제외)
@DynamicUpdate
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne
    @ToString.Exclude
    private Review review;

    @Column(columnDefinition = "datetime") // default : datetime(6) : 밀리,마이크로초까지 / datetime : 초 단위까지
//    @Column(columnDefinition = "datetime(6) default now(6)")
    private LocalDateTime commentedAt;
}