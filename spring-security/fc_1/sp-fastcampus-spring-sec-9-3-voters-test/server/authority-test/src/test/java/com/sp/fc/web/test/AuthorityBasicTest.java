package com.sp.fc.web.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorityBasicTest extends WebIntegrationTest{

    // 스프링 부트 테스트에서는 Mock Test 뿐만 아니라 RestTestTemplate을 이용한 테스트 또한 제공
    // MockTesting 과의 차이는 실제 서블릿 컨테이너 실행 여부이며 RestTestTemplate은 컨테이너를 직접 실행한다.
    TestRestTemplate client;

    @DisplayName("1. greeting 메시지를 불러온다.")
    @Test
    void test_1(){
        client = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> response = client.getForEntity(uri("/greeting/jongwon"), String.class);

        assertEquals("hello jongwon", response.getBody());
        System.out.println(response.getBody());

    }

}
