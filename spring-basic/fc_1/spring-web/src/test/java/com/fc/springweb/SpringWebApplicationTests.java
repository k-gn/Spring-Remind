package com.fc.springweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.springweb.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringWebApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        System.out.println("hello test");

        // # ObjectMapper 직접 사용해보기
        // Text JSON -> Object ( controller req json -> object )
        // Object -> Text JSON ( response object -> json )

        var objectMapper = new ObjectMapper();
        // object -> json
        // object mapper는 get method를 활용한다.
        // 단. 주의할 점으로 아무 메소드 앞에 get이 있다면 직렬화 할 때 에러가 발생한다.
        // 따라서 ObjectMapper가 참조하는 클래스는 getter 로 쓸 메소드가 아니면 앞에 get을 붙이지 말 것!
        var user = new User("steve", 20, "010-1111-1111");
        var text = objectMapper.writeValueAsString(user);
        System.out.println(text);

        // json -> object
        // default 생성자가 필요하다.
        // 제네릭(컬렉션) 객체 시 TypeReference 타입 사용
        var objectUser = objectMapper.readValue(text, User.class);
        System.out.println(objectUser);
    }

}
