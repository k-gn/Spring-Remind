package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // ConcurrentSessionFilter
    // SessionRegistry 에 있는 세션 정보에서 만료된 토큰이 들어오지 못하도록 관리 (동시접속 제어 효과)
    // 요청이 올 때 특정한 세션의 expired 마킹을 해놓으면 들어오지 못하도록 막는다.
    // 그 후 SessionRegistry 로 SessionInformation 을 정리
    // SessionRegistry 는 톰캣에서 제공하는 세션으로 스프링이 SessionInformation 이라는 Wrapper 객체를 만드는데 SessionInformation 을 관리하는 곳
    // 만료된 세션에 대한 요청인 경우 세션 즉시 종료.
    // 세션 만료에 대한 판단은 SessionManagementFilter 의 ConcurrentSessionControlAuthenticationStrategy 에서 처리
    // 서블릿 컨테이너(톰켓)로 부터 HttpSessionEventPublisher 를 리스닝 하도록 listener로 등록
    // 톰켓의 세션과는 별도로 session을 SessionRegistry의 SessionInformation 에서 관리
    // SessionRegistry 의 세션 정보를 expire 시키면 톰켓에서 세션을 아무리 허용하더라도 애플리케이션 내로 들어와서 활동할 수 없다.
    // SessionRegistry 에는 Authentication 으로 등록된 사용자만 모니터링 한다. (인증된 사용자 정보와 세션 아이디를 가지고 있음)

    // SessionManagementFilter (기본적으로 동작함)
    // 어떤 경우에 어떤 세션을 만료시킬 것인지 결정 (사실상 세션 관리를 맡음)
    // 여러가지 세션 인증 정책을 관리하도록 설정할 수 있다.
    // 세션 생성 정책
    // 세션 아이디 고정 설정
    // 동시접근 허용 문제
    // 세션 타임아웃 문제
    // SessionAuthenticationStrategy 인터페이스를 가지고 있음

    // SessionAuthenticationStrategy
    // 인증 발생 시 세션에 어떤 전략을 할건지 정한다.
    // 세션 고정 정책, 동시 접속 제어 전략, csrf


    ExceptionTranslationFilter exceptionTranslationFilter;
    FilterSecurityInterceptor filterSecurityInterceptor;
    AccessDeniedHandlerImpl accessDeniedHandler;

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

    // SessionRegistry 사용을 위한 빈 등록
    @Bean
    SessionRegistry sessionRegistry(){
        // SessionRegistry 구현 클래스
        SessionRegistryImpl registry = new SessionRegistryImpl();
        return registry;
    }

    @Bean
    PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try{
            repository.removeUserTokens("1");
        }catch(Exception ex){
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices("hello",
                        spUserService,
                        tokenRepository()
                        ){
                    @Override
                    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
                        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
//                        return super.createSuccessfulAuthentication(request, user);
                    }
                };
        service.setAlwaysRemember(true); // 항상 동작
        return service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(request->
                    request
                            .antMatchers("/", "/error").permitAll()
                            .antMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated()
                )
                .formLogin(login->
                        login.loginPage("/login") // post
                        .loginProcessingUrl("/loginprocess")
                        .permitAll()
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login-error")
                )
                .logout(logout-> // get, post
                        logout.logoutSuccessUrl("/"))
                .exceptionHandling(error->
                        error
//                                .accessDeniedPage("/access-denied")
                                .accessDeniedHandler(new CustomDeniedHandler()) // 403
                                .authenticationEntryPoint(new CustomEntryPoint()) // 401

                )
                .rememberMe(r->r
//                        .alwaysRemember(true) : 직접 서비스를 만들어서 서비스에서 설정해야함
                        .rememberMeServices(rememberMeServices())
                )
                .sessionManagement(
                        s->s
//                                .sessionCreationPolicy(p-> SessionCreationPolicy.Always) // 세션 생성 정책
                                .sessionFixation(sessionFixationConfigurer -> sessionFixationConfigurer.changeSessionId()) // 고정 세션 보호
                                // 기본적으론 로그인마다 세션이 바뀐다.
                        .maximumSessions(2) // 최대 허용 가능 세션 수, -1인 경우 무제한 세션
                        .maxSessionsPreventsLogin(true) // 동시 로그인 차단, false인 경우 기존 세션 만료 (true 는 들어온 애 차단)
                        .expiredUrl("/session-expired") // 만료 시 동작할 url
                )
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/sessions", "/session/expire", "/session-expired")
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        PathRequest.toH2Console()
                )
        ;
    }

}
