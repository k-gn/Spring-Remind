package com.example.springcalculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/*
    # 주의할 점
    - 패키지가 main과 동일해야 한다.
    - SpringCalculatorApplicationTests 와 SpringCalculatorApplication 가 같은 위치에 존재해야 한다.

    @SpringBootTest : 스프링 컨테이너가 올라가면서 테스트를 가능하게 해주는 어노테이션, 붙이는 순간 모든 Bean이 등록된다.
*/

@SpringBootTest
class SpringJunitTestApplicationTests {

    @Test
    void contextLoads() {
    }

}
