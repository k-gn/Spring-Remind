package com.sp.fc.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/auth") // SecurityContextHolder 를 통해 Authentication 에 접근가능
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/")
    public SecurityMessage hello(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("hello")
                .build();
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/user")
    public SecurityMessage user(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("user page")
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public SecurityMessage admin(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("admin page")
                .build();
    }

}
