package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SpUserService spUserService;
    private final DataSource dataSource;

    public SecurityConfig(SpUserService spUserService, DataSource dataSource) {
        this.spUserService = spUserService;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(spUserService);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    // session state check (세션 리스너 빈 등록)
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher(){
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                super.sessionCreated(event);
                System.out.printf("===>> [%s] 세션 생성됨 %s \n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                super.sessionDestroyed(event);
                System.out.printf("===>> [%s] 세션 만료됨 %s \n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
                super.sessionIdChanged(event, oldSessionId);
                System.out.printf("===>> [%s] 세션 아이디 변경  %s:%s \n",  LocalDateTime.now(), oldSessionId, event.getSession().getId());
            }
        });
    }

    @Bean
    PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try{
            // 하나 지워보고 에러나면 테이블이 없다는 것이니 catch에서 테이블을 생성시켜주기
            repository.removeUserTokens("1");
        }catch(Exception ex){
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    // RememberMeAuthenticationFilter 에서 검증 후 RememberMeAuthenticationToken 을 발급하여 securityContext 에 저장(통행증 발급)
    // securityContextHolder 에선 ThreadLocal 환경으로 securityContext 를 관리하고 ThreadLocal 이 처리를 끝내고 나갈 때 지운다.
    // 인증 후 부턴 세션에서 securityContext 를 securityContextHolder 로 다시 가져오며 끝난 후 지우기를 반복한다.
    // 기본적으론 토큰베이스 서비스를 사용 (내부적으로 userdetailsservice 동작함)
    // PersistentTokenBasedRememberMeServices 를 사용하면 좀 더 보안이 좋다. (토큰을 db에 저장, series:token)
    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices("hello", // 이전 저장값에 대한 유효성 검증을 위한 키값
                        spUserService,
                        tokenRepository()
                        );
        return service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(request->
                    request.antMatchers("/").permitAll()
                            .anyRequest().authenticated()
                )
                .formLogin(login->
                        login.loginPage("/login")
                        .loginProcessingUrl("/loginprocess")
                        .permitAll()
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login-error")
                )
                .logout(logout->
                        logout.logoutSuccessUrl("/"))
                .exceptionHandling(error->
                        error.accessDeniedPage("/access-denied")
                )
                // 세션이 만료되어도 자동로그인을 통해 로그인 가능
                .rememberMe(r->r
                        .rememberMeServices(rememberMeServices())
                )
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        PathRequest.toH2Console()
                )
        ;
    }

}
