package com.example.server.controller;

import com.example.server.dto.Req;
import com.example.server.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/server")
@Slf4j
public class ServerApiController {

    // https://openapi.naver.com/v1/search/local.json?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&sort=random
    @GetMapping("/naver")
    public String naver() {

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", "갈비집")
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "random")
                .encode(Charset.forName("UTF-8"))
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", "NYaopfXA8vmQfJ_bdBK3")
                .header("X-Naver-Client-Secret", "T6Eh7QpHuK")
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        return result.getBody();
    }

    // @RequestParam을 생략하면 내부적으로  String이나 Long 같은 타입은 @ReuqestParam으로 취급하고 그 이외에 파라미터는 @ModelAttribute로 취급
    // body에 담아서 오는 경우 @RequestBody 사용
    // @RequestParam은 1개의 HTTP 요청 파라미터를 받기 위해서 사용
    // @ModelAttribute는 클라이언트가 전송하는 multipart/form-data 형태의 HTTP Body 내용과 HTTP 파라미터들을 Setter를 통해 1대1로 객체에 바인딩
    // @RequestBody는 클라이언트가 전송하는 Json(application/json) 형태의 HTTP Body 내용을 Java Object로 변환시켜주는 역할
    // 공통적으로 받을 타입과 이름이 같아야함

    @GetMapping("/hello")
    public User hello(String name, int age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        return user;
    }

    @PostMapping("/user/{userId}/name/{userName}")
    public Req<User> post(
                     @RequestBody Req<User> user,
                     @PathVariable int userId,
                     @PathVariable String userName,
                     @RequestHeader("x-authorization") String authorization, // HTTP 요청 헤더 값을 컨트롤러 메서드의 파라미터로 전달
                     @RequestHeader("custom-header") String customHeader) {


        log.info("client id : {}", userId);
        log.info("client name : {}", userName);
        log.info("client req : {}", user);
        log.info("client customHeader : {}", customHeader);
        log.info("client authorization : {}", authorization);

        Req<User> response = new Req<>();
        response.setHeader(
                new Req.Header()
        );
        response.setBody(
                user.getBody()
        );

        return response;
    }
}
