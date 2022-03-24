package com.fc.springweb.controller;

import com.fc.springweb.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // REST API 를 처리하는 컨트롤러로 등록
@RequestMapping("/api") // URI 주소 할당
public class ApiController {

    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    } // text/plain

    @GetMapping("/text")
    public String text(String account) {
        return account;
    }

    // JSON 자동 처리
    // req(json) -> object mapper -> object -> method -> object -> object mapper -> json -> resp
    @PostMapping("/json")
    public User json(@RequestBody User user) {
        return user;
    }

    // ResponseEntity를 사용해 상태 코드와 데이터, 헤더 등 값을 같이 내려줄 수 있다. (추천)
    // 내려줄 값을 더욱 명확하게 내려준다.
    @PutMapping("/putApi")
    public ResponseEntity<User> put(@RequestBody User user) {
        System.out.println(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
