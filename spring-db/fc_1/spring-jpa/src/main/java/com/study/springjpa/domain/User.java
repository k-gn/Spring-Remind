package com.study.springjpa.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor // jpa에서 필수
@AllArgsConstructor
@RequiredArgsConstructor // NonNull or final, 다른 consturctor 어노테이션 존재 시 @Data 있어도 작성해줘야함
@Data
// callSuper: 자동 생성 시 부모 클래스의 필드까지 감안할지 안 할지에 대해서 설정
@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
@Builder // 빌더 형식으로 객체를 생성하고 필드값을 주입해준다.
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 롬복에서 제공하는 @NonNull은 메서드의 인자에 사용하면 null이 들어올 시 NullPointerException을 발생시킨다.
    @NonNull
    private String name;

    @NonNull
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
