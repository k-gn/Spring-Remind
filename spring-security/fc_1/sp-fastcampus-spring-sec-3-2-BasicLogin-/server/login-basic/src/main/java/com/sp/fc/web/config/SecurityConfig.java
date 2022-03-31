package com.sp.fc.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 요청이 들어오면 앞단에서 필터들을 체인을 타고 반드시 하나하나 다 거쳐야 한다(스킵 불가능)
// 그런데 시큐리티는 정책에 따라서 여러가지 시큐리티가 공존할 수 있다.
// 경우에 따라 이 시큐리티 또는 저 시큐리티를 적용해야 하기 때문에 필터 체인에 그냥 통채로 들어갈 경우 문제가 발생한다.
// 다양한 시큐리티를 적용하기 위해 시큐리티는 중간에 DelegatingFilterProxy 라는 필터를 만들어 메인 필터체인에 끼워넣고,
// 그 아래 다시 SecurityFilterChain 그룹을 등록해서 사용한다. (url 패턴에 따라 적용되는 필터체인을 다르게 할 수 있다.)
// 선택적으로 필터체인을 스위칭하면서 쓸 수 있다.
// WebSecurityConfigurerAdapter 클래스가 필터 체인을 구성하는 설정 클래스
// 각각의 필터들은 서로 다른 관심사를 해결한다. (단일 책임 원칙 처럼)
// web resource 의 경우 패턴을 따르더라도 필터를 무시(ignore)하고 통과시켜주기도 한다.
@EnableWebSecurity(debug = true) // 디버그 모드로 통과하는 필터체인 확인가능
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1) // 낮은 순으로 먼저 실행
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetail customAuthDetail;

    public SecurityConfig(CustomAuthDetail customAuthDetail) {
        this.customAuthDetail = customAuthDetail;
    }

    // AuthenticationManagerBuilder는 인증에 대한 다양한 설정을 생성 (인증에 대한 처리)
    // AuthenticationManager 재정의
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth    // 메모리에 사용자 등록
                .inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder() // 테스트 용 비밀번호 인코더
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                ).withUser(
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("2222")
                        .roles("ADMIN")
                // 기본적으로 "ROLE _ "접두사를 추가
        );
    }

    // admin 계정도 user 가 할 수 있는 작업을 전부 할 수 있도록 권한 설정
    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    // 선택 일치를 기반으로 리소스 수준에서 웹 기반 보안을 구성
    // 리소스(URL) 접근 권한 설정
    // 인증 전체 흐름에 필요한 Login, Logout 페이지 인증완료 후 페이지 인증 실패 시 이동페이지 등등 설정
    // 인증 로직을 커스텀하기위한 커스텀 필터 설정
    // 기타 csrf, 강제 https 호출 등등 거의 모든 스프링시큐리티의 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 두가지 이상의 요청 처리를 받는 필터체인을 구성하려면 이런 필터체인 설정파일을 하나 더 구성하면 된다. (단 순서가 중요해서 Order 선언 필수)
//        http.antMatcher("/api/**"); // 특정 요청만 받는 필터 체인
        http.csrf().disable();
        http    // 시큐리티 처리에 HttpServletRequest를 이용
                .authorizeRequests(request->
                    request.antMatchers("/").permitAll()
                            .anyRequest().authenticated()
                )
                // form 태그 기반의 로그인을 지원하겠다는 설정
                .formLogin(login->
                        login.loginPage("/login")
                        .loginProcessingUrl("/loginprocess")
                        .permitAll() // 로그인 페이지에 접근하기 위한 설정
                        .defaultSuccessUrl("/", false)
                        .authenticationDetailsSource(customAuthDetail) // details에 대한 정보를 커스터 마이징하여 셋팅할 때 사용
                        .failureUrl("/login-error")
                )
                .logout(logout->
                        logout.logoutSuccessUrl("/"))
                .exceptionHandling(error->
                        error.accessDeniedPage("/access-denied")
                )
                ;
    }

    // 전역 보안에 영향을주는 구성 설정에 사용됩니다 (자원 무시, 디버그 모드 설정, 사용자 지정 방화벽 정의를 구현하여 요청 거부 등)
    // HTTP를 적용하기 전에 시큐리티 필터를 적용할지 말지를 먼저 결정
    // 스프링 시큐리티를 필터를 사용하기 전에 적용된 패턴을 다 걸러냄
    // 서버가 일을 조금이라도 일을 덜하게 하기 위해 정적인 리소스들은 웹 필터로 걸러주는 것을 권장
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 스프링 시큐리티 룰을 무시하게 하는 Url 규칙
        // resources (html, css ..) pass
        web.ignoring()
                .requestMatchers(
                        // 스프링 부트가 제공하는 static 리소스들의 기본위치를 다 가져와서 스프링 시큐리티에서 제외
                        PathRequest.toStaticResources().atCommonLocations()
                )
        ;
    }

}
