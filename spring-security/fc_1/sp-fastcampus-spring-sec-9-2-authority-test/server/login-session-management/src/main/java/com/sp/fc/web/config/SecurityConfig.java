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

    @Bean
    SessionRegistry sessionRegistry(){
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
                new PersistentTokenBasedRememberMeServices("hello",  spUserService, tokenRepository()) {
                    // deny 발생 시 익명 사용자가 아닌 rememberMe 토큰을 받아 사용하는 사용자도 로그인 창으로 리다이렉트 되는 문제를 해결하기 위해
                    // createSuccessfulAuthentication 를 오버라이딩 해준다.
                    // rememberMe 토큰이 아닌 UsernamePasswordAuthenticationToken 를 리턴해서 속여줄 수 있다.
                    // 단, 사이트 운영 정책에 따라 다를 테니 상황에 맞게 쓰면 될듯
                    // 스프링에선 rememberMeAuthentication 사용자를 fullAuthentication 과 별도로 취급하길 의도한 것 같음
                        @Override
                        protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
                            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
//                            return super.createSuccessfulAuthentication(request, user);
                        }
                };
        service.setAlwaysRemember(true);
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
                        login.loginPage("/login")
                        .loginProcessingUrl("/loginprocess")
                        .permitAll()
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login-error")
                )
                .logout(logout->
                        logout.logoutSuccessUrl("/"))
                .exceptionHandling(error->
                        error
//                                .accessDeniedPage("/access-denied")
                                .accessDeniedHandler(new CustomDeniedHandler())
                                .authenticationEntryPoint(new CustomEntryPoint())

                )
                .rememberMe(r->r
                        .rememberMeServices(rememberMeServices())
                )
                .sessionManagement(
                        s->s
//                                .sessionCreationPolicy(p-> SessionCreationPolicy.S)
                                .sessionFixation(sessionFixationConfigurer -> sessionFixationConfigurer.changeSessionId())
                        .maximumSessions(2)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/session-expired")
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
