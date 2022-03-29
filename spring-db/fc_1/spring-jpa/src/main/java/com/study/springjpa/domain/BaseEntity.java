package com.study.springjpa.domain;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.study.springjpa.domain.listener.Auditable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// 중복 제거를 위해 추상화한 클래스
@Data
@MappedSuperclass // 해당 클래스의 필드를 상속받는 엔티티 컬럼으로 포함
// 등록 및 수정일 등록같은 날짜 관련된 리스너는 워낙 자주 쓰여서 스프링에서 별도의 기본 리스너를 제공하고 있다.
@EntityListeners(value = AuditingEntityListener.class) // 이미 스프링에서 만들어 놓은 엔티티 리스너를 사용 (Auditing : 감시)
public class BaseEntity implements Auditable {

    // | now()               | now(3)                  | now(6)                    |
    //+---------------------+-------------------------+----------------------------+
    //| 2015-07-24 03:32:30 | 2015-07-24 03:32:30.155 | 2015-07-24 03:32:30.155405 |

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "datetime default now() comment '생성시간'")
    // columnDefinition : 해당 컬럼의 타입, default 지정 가능, auto ddl 할 때 주는 추가적인 속성값
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "datetime(6) default now(6) comment '수정시간'") // 초단위를 최대 6자리
    private LocalDateTime updatedAt;
}