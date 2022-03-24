package com.fc.springweb.controller;

import com.fc.springweb.dto.UserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/get")
public class GetApiController {

    // Get (조회, 멱등함)
    @GetMapping(path = "/hello")
    public String hello() {
        return "get Hello";
    }

    @RequestMapping(path = "/hi", method = RequestMethod.GET)
    public String hi() {
        return "get Hi";
    }

    // @PathVariable : 경로에서 파라미터 가져오기
    // 변수명을 맞추거나 name 속성 사용
    @GetMapping("/path-variable/{id}")
    public String pathVariable(@PathVariable(name = "id") String pathName) {
        System.out.println("PathVariable : " + pathName);
        return pathName; // echo
    }

    // Query-Parameter (?key=value&key=value...)
    // 1. @RequestParam 를 통해 컬렉션 객체 등에 쿼리 파라미터를 받을 수 있다.
    @GetMapping(path = "/query-param")
    public String queryParam(@RequestParam Map<String, Object> queryParam) {
        StringBuilder sb = new StringBuilder();
        queryParam.entrySet().forEach(entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("\n");

            sb.append(entry.getKey() + " = " + entry.getValue() + "\n");
        });
        return sb.toString();
    }

    @GetMapping(path = "/query-list")
    public String queryList(@RequestParam List<String> names) { // list 로 받을 때 변수명이 같아야함
        StringBuilder sb = new StringBuilder();
        for(String s : names) {
            sb.append(s + " ");
        }
        return sb.toString();
    }

    // 2. 쿼리 파라미터 key 명으로 명시적으로 받기 (@RequestParam 생략 가능)
    @GetMapping("/query-param02")
    public String queryParam02(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam int age) {
        return name + " " + email + " " + age;
    }

    @GetMapping("/query-param03")
    public String queryParam03(String name, String email, int age) {
        return name + " " + email + " " + age;
    }

    // 3. 커맨드 객체로 받기 (추천!)
    // @RequestParam 이 없어도 Spring 이 알아서 매핑해준다.
    // 들어오는 쿼리파라미터 이름을 클래스의 변수명과 매핑 (이 때 setter 동작)
    @GetMapping("/query-param04")
    public String queryParam04(UserRequest userRequest) {
        return userRequest.toString();
    }
}
