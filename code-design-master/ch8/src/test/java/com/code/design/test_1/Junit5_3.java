package com.code.design.test_1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Junit5_3 {

    @Test
    @Order(2) // 테스트 코드 순서 보장(값이 높을 수록 낮은 우선순위), 단 최대한 순서에 영향이 없도록 먼저 테스트를 짜는 것이 중요하다.
    void test_1() {
        System.out.println("test_1");
    }

    @Test
    @Order(1)
    void test_2() {
        System.out.println("test_2");
    }

    @Test
    @Order(99)
    void test_3() {
        System.out.println("test_3");
    }
}