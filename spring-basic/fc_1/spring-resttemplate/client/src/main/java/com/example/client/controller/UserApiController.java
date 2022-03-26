package com.example.client.controller;

import com.example.client.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("")
    public void get(){
        userService.naver();
    }

}
