package com.sp.fc.web;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//  # webEnvironment : 테스트의 웹 환경을 설정하는 속성이며 기본값은 SpringBootTest.WebEnvironment.MOCK
//        MOCK : mock servlet environment으로 내장 톰캣 구동을 안한다
//        RANDOM_PORT, DEFINED_PORT : 내장 톰캣 사용
//        NONE : 서블릿 환경 제공 안함
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationTest {

    @LocalServerPort // 실제 주입되는 포트번호를 확인
    int port;

    // RestTemplate
    // Spring 3.0 부터 지원, 스프링이 제공하는 HTTP 통신에 유용하게 사용 할 수 있는 템플릿이며, HTTP 서버와의 통신을 단순화하고 RESTful 원칙을 지키고 있다.
    RestTemplate client = new RestTemplate();

    private String greetingUrl(){
        return "http://localhost:"+port+"/greeting";
    }

    @DisplayName("1. 인증 실패")
    @Test
    void test_1(){
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getForObject(greetingUrl(), String.class);
        });
        assertEquals(401, exception.getRawStatusCode());
    }


    @DisplayName("2. 인증 성공")
    @Test
    void test_2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);
        assertEquals("hello", resp.getBody());
    }

    @DisplayName("3. 인증성공2 ")
    @Test
    void test_3() {
        // TestRestTemplate
        // RestTemplate 의 테스트를 위한 버전 (RestTemplate 에 대한 통합 테스트를 단순화, 테스트 중에 인증을 용이하게 한다.)
        // 기본으로 Basic Token을 지원한다. ( basic header token을 알아서 넣어서 request를 날려준다.)
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", resp);
    }

    @DisplayName("4. POST 인증") // 데이터에 변경을 가하는 작업
    @Test
    void test_4() {
        // BODY 안에 데이터를 한번에 보내야 한다. @RequestBody 를 두번 써서 받을 수 없는 듯.
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> resp = testClient.postForEntity(greetingUrl(), "jongwon", String.class);
        assertEquals("hello jongwon", resp.getBody());
    }
    
}
