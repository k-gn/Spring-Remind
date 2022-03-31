package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.TeacherManager;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final StudentManager studentManager;
    private final TeacherManager teacherManager;

    public SecurityConfig(StudentManager studentManager, TeacherManager teacherManager) {
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
    }


    /* * 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체.
    * 인증에 대한 지원을 설정 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // provider 를 등록하면 필요 시 알아서 순서대로 실행된다.
        auth.authenticationProvider(studentManager);
        auth.authenticationProvider(teacherManager);
    }

    /*
     * 스프링 시큐리티 규칙
     * 대부분의 설정
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        http
                .authorizeRequests(request->
                        request.antMatchers("/", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin( // UsernamePasswordAuthenticationFilter 동작
                        login->login.loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login-error")
                )
                // UsernamePasswordAuthenticationFilter 위치에 customLoginFilter 등록
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->logout.logoutSuccessUrl("/"))
                .exceptionHandling(e->e.accessDeniedPage("/access-denied"))
                ;

        // UsernamePasswordAuthenticationFilter 와 customLoginFilter 를 둘다 허용해도 customLoginFilter 가 먼저 수행되서 큰 문제가 없다.
    }

    /* * 스프링 시큐리티 룰을 무시하게 하는 Url 규칙(여기 등록하면 규칙 적용하지 않음)
    * 스프링시큐리티 앞단 설정들을 할 수 있다. */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                ;
    }
}
