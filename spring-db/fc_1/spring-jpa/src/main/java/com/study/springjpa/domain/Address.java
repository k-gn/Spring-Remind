package com.study.springjpa.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

// # 임베디드 타입
// 새로운 값 타입을 직접 정의해서 사용 (직접 정의한 임베디드 타입도 int, String처럼 값 타입)
// @Embeddable: 값 타입을 정의하는 곳에 표시
// @Embedded: 값 타입을 사용하는 곳에 표시
// 임베디드 타입은 기본 생성자가 필수 
// 같은 도메인의 필드값들의 묶음
//@Entity
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
//    @Id
//    private Long id;

    private String city;    // 시 (enum 같은거로 해도 좋을듯)

    private String district;    // 구

    @Column(name = "address_detail")
    private String detail;  // 상세주소

    private String zipCode; // 우편번호
}