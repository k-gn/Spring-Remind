package com.sp.fc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// Spring Security 사용을 위해서는 @EnableWebSecurity 어노테이션을 달아줘야 합니다
// WebSecurityConfigurerAdapter를 상속받은 config 클래스에 @EnableWebSecurity 어노테이션을 달면
// SpringSecurityFilterChain이 자동으로 포함 + 스프링 시큐리티 설정 활성화, 시큐리티 필터 추가 = 활성화 된 시큐리티 설정을 해당 파일에서 하겠다,
// 기본적인 Web 보안을 활성화
// + @AuthenticationPrincipal 어노테이션을 통해 Authentication 객체 속에 들어있는 principal 필드를 가져올 수 있게해준다.
// @EnableWebSecurity 어노테이션과 WebSecurityConfigurerAdapter 은 웹기반 보안을 위해 함께 작동
// CSRF 공격을 방지하는 기능을 지원
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메소드 시큐리티를 사용하기 위한 선언
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 설정은 WebSecurityConfigurerAdapter 클래스를 상속받아 오버라이딩하는 방식으로 진행

    @Bean
    UserDetailsService userService(){
        // Spring Security에서는 비밀번호를 안전하게 저장할 수 있도록 비밀번호의 단방향 암호화를 지원하는 PasswordEncoder 인터페이스와 구현체들을 제공
        // BcryptPasswordEncoder : BCrypt 해시 함수를 사용해 비밀번호를 암호화
        // Argon2PasswordEncoder : Argon2 해시 함수를 사용해 비밀번호를 암호화
        // Pbkdf2PasswordEncoder : PBKDF2 해시 함수를 사용해 비밀번호를 암호화
        // SCryptPasswordEncoder : SCrypt 해시 함수를 사용해 비밀번호를 암호화
        final PasswordEncoder pw = passwordEncoder(); // 비밀번호 인코딩 필수 (패스워드를 그대로 DB에 넣으면 안된다.)
        UserDetails user2 = User.builder()
                .username("user2")
                .password(pw.encode("1234"))
                .roles("USER").build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(pw.encode("1234"))
                .roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user2, admin);
    }

    // 패스워드 인코더 빈 등록
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .and()
                .authorizeRequests(auth->{
                    auth.anyRequest().permitAll();
                })
                ;
    }
}
