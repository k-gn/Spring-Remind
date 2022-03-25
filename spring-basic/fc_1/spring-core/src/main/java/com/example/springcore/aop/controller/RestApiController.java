package com.example.springcore.aop.controller;

import com.example.springcore.aop.annotation.Decode;
import com.example.springcore.aop.annotation.Timer;
import com.example.springcore.aop.dto.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis")
public class RestApiController {

    @GetMapping("/get/{id}")
    public String get(@PathVariable Long id, String name) {
        System.out.println("get method");
        return id + " " + name;
    }

    @PostMapping("/post")
    public User post(@RequestBody User user) {
        System.out.println("post method");
        return user;
    }

    @Timer
    @DeleteMapping("/delete")
    public void delete() throws InterruptedException {
        Thread.sleep(1000 * 2);
    }

    @Decode
    @PutMapping("/put")
    public User put(@RequestBody User user) {
        System.out.println("put method");
        System.out.println(user);
        return user;
    }
}