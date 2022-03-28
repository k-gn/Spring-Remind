package com.study.springjpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }

    // 엔티티를 리턴 시
    // json 으로 변경하는 과정에서 엔티티의 get이 호출되고
    // 이 과정에서 변환 오류나, 무한 참조가 발생할 수 있다.
    // 맘편히 DTO 만들어서 리턴하는게 젤 좋아보인다.
}
