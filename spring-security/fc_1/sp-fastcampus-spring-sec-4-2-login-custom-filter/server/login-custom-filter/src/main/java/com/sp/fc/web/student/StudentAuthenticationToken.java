package com.sp.fc.web.student;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Student 통행증 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAuthenticationToken implements Authentication {

    private Student principal;
    private String credentials;
    private String details;
    private boolean authenticated;
    private Set<GrantedAuthority> authorities;


    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }

}
// Authentication
// 통행증 같은 역할
// ~Token 이름을 가진 구현체들이 있다.
// 인증을 하거나 또는 받은 객체가 들어있다.