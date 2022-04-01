package com.sp.fc.web.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 매번 들어오는 요청에 토큰을 검사해서 시큐리티 컨텍스트에 넣어주는 필터
public class JWTCheckFilter extends BasicAuthenticationFilter { // Spring에서 제공해주는 인증 필터, http 헤더를 검증 (GenericFilterBean을 상속받아 사용해도 된다)

    private SpUserService userService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, SpUserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("doFilterInternal -----------------------------------------------------------");
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearer == null || !bearer.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }
        String token = bearer.substring("Bearer ".length());
        VerifyResult result = JWTUtil.verify(token);
        if(result.isSuccess()){
            SpUser user = (SpUser) userService.loadUserByUsername(result.getUsername());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(userToken);
            chain.doFilter(request, response); // 다음 필터로 넘김
        }else{
            throw new TokenExpiredException("Token is not valid");
        }
    }

}
