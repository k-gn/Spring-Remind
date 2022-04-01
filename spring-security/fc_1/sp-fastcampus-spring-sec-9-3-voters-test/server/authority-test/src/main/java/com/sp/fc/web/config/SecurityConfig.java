package com.sp.fc.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.Collection;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                        .username("user1")
                        .password("1111")
                        .roles("USER", "STUDENT")
                )
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user2")
                                .password("1111")
                                .roles("USER", "STUDENT")
                )
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("tutor1")
                                .password("1111")
                                .roles("USER", "TUTOR")
                )
                ;
    }

    FilterSecurityInterceptor filterSecurityInterceptor;

    // 스프링에서 권한은 기본적으로 Voter 기반의 AccessDecisionManager 가 처리
    // 하지만 실제론 WebExpressionVoter 나 PIAAVoter 로 하는 권한 체크로 몰리고 있다.
    // WebExpressionVoter -> SecurityExpressionHandler (createEvaluationContext) -> WebSecurityExpressionRoot
    // PIAAVoter -> MethodSecurityExpressionHandler (createSecurityExpressionRoot) -> MethodSecurityExpressionRoot
    // SpEL 을 써야하기 때문에 parser 가 Context 를 핸들링 해야한다. 따라서 각각 핸들러 가지고 있다.
    // 또한 각각 루트객체가 존재해야해서 createEvaluationContext 와 createSecurityExpressionRoot 메소드를 사용해여 생성
    // WebSecurityExpressionRoot 은 hasIpAddress(String) 메소드가 추가로 제공되고
    // MethodSecurityExpressionRoot 는 filterObject 과 returnObject 에 바로 접근이 가능하다. (# 없이 접근이 가능)
    // PathVariable 은 Context 에서 variable 로 취급해서 # 으로 검사 가능
    // 빈은 @ 를 붙여서 검사 가능

    AccessDecisionManager filterAccessDecisionManager(){
        return new AccessDecisionManager() {
            @Override
            public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
                throw new AccessDeniedException("접근 금지");
//                return;
            }

            @Override
            public boolean supports(ConfigAttribute attribute) {
                return true;
            }

            @Override
            public boolean supports(Class<?> clazz) {
                return FilterInvocation.class.isAssignableFrom(clazz);
            }
        };
    }

    @Autowired
    private NameCheck nameCheck;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().and()
                .authorizeRequests(
                        authority->authority
                                .mvcMatchers("/greeting/{name}")
                                    .access("@nameCheck.check(#name)") // 특정 이름만 통과하도록 설정
                                .anyRequest().authenticated()
//                        .accessDecisionManager(filterAccessDecisionManager())
                )
                ;
    }
}

// SpEL : 컴퍼일 언어를 스크립트 처럼 동작하게 하는 표현식