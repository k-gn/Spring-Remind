package com.fc.springweb.controller;

import com.fc.springweb.dto.PostRequestDto;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostApiController {

    // Post 방식 (등록, 멱등하지 않음)
    // @RequestBody : Post 방식에서 Json으로 받은 HTTP Body 데이터를 받을 수(parsing) 있다.
    @PostMapping("/post")
    public void post(@RequestBody MultiValueMap<String, String> requestData, HttpServletRequest request) {
        System.out.println(request.getHeader("Content-Type"));;
        System.out.println(requestData);
        requestData.forEach((key, value) -> { // map을 람다로 더 간단하게 추출 가능
            System.out.println("key : " + key);
            System.out.println("value : " + value);
        });
    }

    // MultiValueMap은 Spring에서 제공하는 Data Collection으로 List형태의 값들을 Value로 Binding 할 수 있는 특징을 지닌다.
    // body 로 올 경우 application/x-www-form-urlencoded 로 정의된 폼 데이터를 주고받는다.
    // 같은 Key를 가진 파라미터 값이 여러개일 경우를 대비할 수 있다.
    @GetMapping("/post/get")
    public void get(@RequestParam MultiValueMap<String, String> requestData, HttpServletRequest request) {
        System.out.println(requestData);
        System.out.println(request.getHeader("Content-Type"));
    }

    @PostMapping("/post02")
    public void post02(@RequestBody PostRequestDto requestData) {
        System.out.println(requestData);
    }
}

    /*
        # 대부분 현업에선 JSON 데이터를 주고받는다.
        # JSON 형태

        String : value
        number : value
        boolean : value
        object : value { .. }
        array : value [ .. ]

        # 단어 구분은 카멜 or 스네이크 케이스를 사용한다.

        ex1 object)
        {
            "phone_number" : "010-1111-2222",
            "age" : 10,
            "isAgree" : false,
            "account" : {
                "email" : "aaa@aaa.com",
                "password" : "1234"
            }
        }
        ex2 array)
        {
            "user_list" : [
                { .. },
                { .. },
                { .. },
                ...
            ]
        }
    */