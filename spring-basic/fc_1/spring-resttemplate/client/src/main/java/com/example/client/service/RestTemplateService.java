package com.example.client.service;

import com.example.client.dto.Req;
import com.example.client.dto.UserRequest;
import com.example.client.dto.UserResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

// Server to Server
@Service
public class RestTemplateService {

    public UserResponse hello() {

        // uri 생성
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/hello")
                .queryParam("name", "steve")
                .queryParam("age", 10)
                .encode()
                .build()
                .toUri();

        System.out.println(uri.toString());

        RestTemplate restTemplate = new RestTemplate(); //스프링에서 제공하는 http 통신에 유용하게 쓸 수 있는 템플릿

//        String result = restTemplate.getForObject(uri, String.class); // 지정한 타입으로 리턴
//        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        ResponseEntity<UserResponse> result = restTemplate.getForEntity(uri, UserResponse.class); // ResponseEntity로 리턴
        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());

        return result.getBody();
    }


    public UserResponse post() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve") // pathVariable 와 순서대로 매핑된다.
                .toUri();
        System.out.println(uri.toString());

        // 보낼 데이터
        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);

        RestTemplate restTemplate = new RestTemplate();
        // 서버가 어떤 타입으로 내려줄지 모르면 일단 String으로 받는것도 하나의 전략
        // postForObject
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri, req, UserResponse.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());

        return response.getBody();
    }


    public UserResponse exchange() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve") // pathVariable 와 순서대로 매핑된다.
                .toUri();
        System.out.println(uri.toString());

        // 보낼 데이터
        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);

        // 일반적으로 필요한 헤더값을 같이 보내주는 경우가 많다.
        // add header
        RequestEntity<UserRequest> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "abcd")
                .header("custom-header", "ffffff")
                .body(req);


        RestTemplate restTemplate = new RestTemplate();
        // exchange
        // HTTP 헤더를 새로 만들 수 있고 어떤 HTTP 메서드도 사용가능
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity, UserResponse.class);
        return response.getBody();
    }

    // 현업에선 header 와 body 가 유동적일 경우가 많다, 따라서 기본틀을 만들어 놓고 제네릭을 사용해 재사용성을 높이는 방법
    public Req<UserResponse> genericExchange() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve") // pathVariable 와 순서대로 매핑된다.
                .toUri();
        System.out.println(uri.toString());

        // 보낼 데이터
        UserRequest userRequest = new UserRequest();
        userRequest.setName("steve");
        userRequest.setAge(10);

        Req<UserRequest> req = new Req<>();
        req.setHeader(
                new Req.Header()
        );

        // body 에 들어갈 클래스만 바꿔주면 된다.
        req.setBody(
                userRequest
        );

        // add header
        RequestEntity<Req<UserRequest>> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "abcd")
                .header("custom-header", "ffffff")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();

        // 제네릭 클래스는 .class 를 붙일 수 없다.
        // 따라서 ParameterizedTypeReference 를 사용하면 제네릭 타입으로 받을 수 있다.
        // 그냥 TypeReference 도 있는데 json -> generic collection 에서 일단 사용 가능했음
        ResponseEntity<Req<UserResponse>> response
                = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        return response.getBody();
    }
}
