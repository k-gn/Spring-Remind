package com.fc.springweb.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DeleteApiController {

    // delete -> 리소스 삭제 200 ok, 멱등함
    @DeleteMapping("/delete/{userId}")
    public void delete(@PathVariable String userId, String account) {
        System.out.println(userId);
        System.out.println(account);
    }
}