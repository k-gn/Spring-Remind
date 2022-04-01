package com.sp.fc.web.config;


import com.sp.fc.user.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class OnlinePaperSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserSecurityService userSecurityService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }

    // rememberService 를 직접 만들 시 이 안에서 관련 설정을 해줘야 한다.
    private RememberMeServices rememberMeServices(){
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
                "paper-site-remember-me",
                userSecurityService
        );
        rememberMeServices.setParameter("remember-me");
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(3600);
        return rememberMeServices;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final SpLoginFilter filter = new SpLoginFilter(
                authenticationManagerBean(),
                rememberMeServices()
        );
        http
                .csrf().disable()
                .formLogin(login->{
                    login.loginPage("/login") // custom login page url (get request)
                    ;
                })
                .logout(logout->{
                    logout.logoutSuccessUrl("/")
                    ;
                })
                .rememberMe(config->{
//                            config.rememberMeParameter("remember-me")
//                                    .key("papaer-site")
//                                    .tokenValiditySeconds(3600)
//                                    .userDetailsService(userSecurityService)
//                                    .alwaysRemember(true)
                            config.rememberMeServices(rememberMeServices())
                            ;
                })
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception->{
                    exception.accessDeniedPage("/access-denied");
                })
                .authorizeRequests(config->{
                    config
                            // /*  패턴은 그러니까 /, /aaa, /bbb 등등에 맵핑이 되지만 /aaa/bbb는 /*에 맵핑이 안되고, /**에는 맵핑된다.
                            .antMatchers("/").permitAll()
                            .antMatchers("/login").permitAll()
                            .antMatchers("/error").permitAll()
                            .antMatchers("/signup/*").permitAll()
                            .antMatchers("/study/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STUDENT")
                            .antMatchers("/teacher/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER")
                            .antMatchers("/manager/**").hasAuthority("ROLE_ADMIN")
                    ;
                })
        ;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}
