package com.example.filter.controller;

import com.example.filter.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/apif/temp")
public class ApiUserController {

    @PostMapping("")
    public User user(@RequestBody User user) {
        log.info("user : {}", user); // {} 에 , 뒤에 값이 차례대로 들어간다.
        return user;
    }

}
