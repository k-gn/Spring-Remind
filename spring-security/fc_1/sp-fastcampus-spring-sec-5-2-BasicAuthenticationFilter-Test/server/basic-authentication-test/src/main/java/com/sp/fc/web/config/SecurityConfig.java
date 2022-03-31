package com.sp.fc.web.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    BasicAuthenticationFilter filter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                        .username("user1")
                        .password("1111")
                        .roles("USER")
                        .build()
                );
    }

    // authorizeRequests() : 시큐리티 처리에 HttpServletRequest를 이용 - 요청에 대한 권한을 지정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                // Basic 방식 적용 - Basic Authentication(REST API 인증을 할 때, username, password로 인증을 하는 방법)
                // 브라우저가 표시하는 대화창 (브라우저에 따라 디자인은 다르지만 기능은 같음), SPA 페이지
                // Spring에서 API를 제공해야 할 때, 선택할 수 있는 방식
                // 서버에서 내려주는 로그인 페이지 (폼 로그인) 을 사용할 수 없는 상황에서 사용한다.
                // ex) 자바 스크립트를 통해서 인증 시 사용 가능
                .httpBasic()
                ;
    }

}
