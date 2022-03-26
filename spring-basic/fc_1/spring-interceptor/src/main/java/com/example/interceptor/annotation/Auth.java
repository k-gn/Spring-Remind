package com.example.interceptor.annotation;

import java.lang.annotation.*;

/* * 커스텀 어노테이션
 * Documented : javadoc으로 문서 생성 시 현재 어노테이션 설명 추가 <br/>
 * Retention : 어노테이션을 유지하는 정책 설정<br/>
 * Target : 어노테이션 적용 위치
 * */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Auth {

}
