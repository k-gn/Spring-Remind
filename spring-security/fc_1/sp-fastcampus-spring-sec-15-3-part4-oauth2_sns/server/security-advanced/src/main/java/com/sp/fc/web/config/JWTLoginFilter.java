package com.sp.fc.web.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 유효한 사용자라면 인증 토큰을 내려주는 필터
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private SpUserService userService;

    public JWTLoginFilter(AuthenticationManager authenticationManager, SpUserService userService) {
        super(authenticationManager);
        this.userService = userService;
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows // @SneakyThrows 어노테이션을 사용하면 명시적인 예외 처리를 생략할 수 있다.
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException
    {
        // HTTP Request 중 message-body로 넘어온 parameter 확인을 위해서는 getInputStream() 이나 getReader()를 사용
        // objectMapper 를 통해 ServletInputStream 을 바로 UserLoginForm 로 받을 수 있다.
        System.out.println("attemptAuthentication -----------------------------------------------------------");
        UserLoginForm userLogin = objectMapper.readValue(request.getInputStream(), UserLoginForm.class);
        if(userLogin.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userLogin.getUsername(), userLogin.getPassword(), null
            );
            // user details...
            return getAuthenticationManager().authenticate(token);
        }else{
            VerifyResult verify = JWTUtil.verify(userLogin.getRefreshToken());
            if(verify.isSuccess()){
                SpUser user = (SpUser) userService.loadUserByUsername(verify.getUsername());
                return new UsernamePasswordAuthenticationToken(
                        user, user.getAuthorities()
                );
            }else{
                throw new TokenExpiredException("refresh token expired");
            }
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException
    {
        SpUser user = (SpUser) authResult.getPrincipal();
        response.setHeader("auth_token", JWTUtil.makeAuthToken(user));
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(user));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}
