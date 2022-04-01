package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpOauth2User;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SnsLoginSecurityConfig extends WebSecurityConfigurerAdapter {

    // OAuth2
    // 구글이나, 페이스북, 네이버, 카카오 같은 이름있는 곳에 가입한 사람들이 많음
    // 해당 사이트로 로그인 후 이런 사이트에 가입한 사람들의 개인정보를 위임받아 가입하도록 하는 방식
    // 스프링은 기본적으로 구글, 깃허브, 페이스북 OAuth2Provider 를 제공한다.

    @Autowired
    private SpUserService userService;

    @Autowired
    private SpOAuth2UserService oAuth2UserService; // except google

    @Autowired
    private SpOidcUserService oidcUserService; // google

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .formLogin().and()
                .oauth2Login( // use oauth login
                        oauth2->
                                oauth2.userInfoEndpoint(userInfo-> //  OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
                                    userInfo.userService(oAuth2UserService) // oauth2 service 등록
                                            .oidcUserService(oidcUserService)
                                )
                                        .successHandler(new AuthenticationSuccessHandler() {
                                            @Override
                                            public void onAuthenticationSuccess(
                                                    HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    Authentication authentication
                                            ) throws IOException, ServletException {

                                                Object principal = authentication.getPrincipal();

                                                if(principal instanceof OAuth2User){
                                                    if(principal instanceof OidcUser){
                                                        // google
                                                        SpOauth2User googleUser = SpOauth2User.OAuth2Provider.google.convert((OAuth2User) principal);
                                                        SpUser user = userService.loadUser(googleUser);
                                                        SecurityContextHolder.getContext().setAuthentication(
                                                                new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities())
                                                        );
                                                    }else{
                                                        // naver, or kakao, facebook
                                                        SpOauth2User naverUser = SpOauth2User.OAuth2Provider.naver.convert((OAuth2User) principal);
                                                        SpUser user = userService.loadUser(naverUser);
                                                        SecurityContextHolder.getContext().setAuthentication(
                                                                new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities())
                                                        );
                                                    }
                                                    System.out.println(principal);
                                                    request.getRequestDispatcher("/").forward(request, response);
                                                }

                                            }
                                        })

//                        .and()
//                        .addFilterAfter(userTranslateFilter, OAuth2LoginAuthenticationFilter.class)
                )

                ;
    }
}
