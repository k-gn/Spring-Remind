package com.study.springjpa.domain;


import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Martin
 * @since 2021/03/31
 */
@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@EntityListeners(value = AuditingEntityListener.class)
public class UserHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "user_id", insertable = false, updatable = false), onetomany 쪽에서 지정한 joincolumn 명과 같아야함
//    private Long userId; // jpa가 user_id, userId 값 사이에서 못찾을 수 있어서 외래키 컬럼을 명시할 경우 이름을 맞춰주는게 중요

    private String name;

    private String email;

    @ManyToOne // many 쪽에 자동으로 fk를 만들어준다.
    @ToString.Exclude
    private User user;

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

//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
}