package com.example.client.controller;

import com.example.client.dto.Req;
import com.example.client.dto.UserResponse;
import com.example.client.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientApiController {

    private final RestTemplateService restTemplateService;

    public ClientApiController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @GetMapping("/hello")
    public UserResponse hello() {
        return restTemplateService.hello();
    }

    @GetMapping("")
    public Req<UserResponse> getHello() {
//        return restTemplateService.hello();
//        return restTemplateService.exchange();
        return restTemplateService.genericExchange();
    }

    @PostMapping("")
    public UserResponse post() {
        return restTemplateService.post();
    }
}
