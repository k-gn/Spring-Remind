package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdvancedSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpUserService userService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    // 스프링 시큐리티에서 다루는 토큰 : JWT (Json Web Token)
    // 일반적으로 Payload 에 Claim 이란 데이터들을 key value 쌍으로 가지고 있는 데이터를 서버 끼리 주고받는 규약
    // Payload 에 데이터를 신뢰하기 위해 (작성한 사람이 작성한 대로 위변조 되지 않음을 보장) 서명값을 붙인다.

    // 일반적으로 서버는 사용자가 인증 후 유지시키기 위해 세션을 사용한다.
    // 하지만 서버가 여러개 이거나 서로 다른 도메인의 데이터를 요청(sso)하는 경우 세션을 유지하는 비용이 커진다. (기본적으로 다른 서버간 세션을 공유하지 못함)
    // 싱글사인온(Single Sign-On, SSO)은 하나의 로그인 인증 정보를 사용해 여러 애플리케이션에 접근할 수 있는 중앙화된 세션 및 사용자 인증 서비스
    // 다른 서버에는 내 서버의 세션이 없기 때문에 내 세션을 유지시킬 방법 또한 쉽지 않음
    // 따라서 토큰을 사용한다. (쉽게 여러대의 서버에서 사용자 정보를 공유하기 위해)
    // java-jwt or jjwt 라이브러리를 사용한다.
    // 서버가 jwt 토큰을 생성해 내려주면 클라이언트가 jwt 토큰을 가지고 있다가 매 요청마다 토큰을 같이 보내고, 서버는 다시 토큰을 검증해 사용자 인증 후 응답 처리
    // 토큰을 쓴다고 세션을 사용할 수 없는건 아니다. 하지만 굳이 그렇게 쓸 이유는 없다. (세션을 대체하는 이유로 토큰을 사용하는 것이기 때문)

    // 인증 토큰 이외에 refresh token 을 내려줘서 만료되어도 인증 토큰 일정 시간동안 갱신 받도록 해줄 수 있다.
    // 자동 로그인 또한 구현 가능
    // 클라이언트가 토큰을 저장, 관리하기 때문에 refresh token 이 탈취될 가능성이 있다. (db로 관리 또는 추적 관리하는 기법이 필요할 수 있다.)
    // 따라서 실제로 서버는 사용자 정보 캐싱이나 토큰의 유효성 평가, 혹은 refresh 토큰 정책을 위해 서버에 토큰을 관리하기도 한다.
    // 이 경우, 토큰과 사용자 정보를 관리하는 방법으로 redis, hazelcast, db저장 등의 방법들을 사용한다.
    // db 저장 방법은 매 요청마다 db 에서 사용자 정보를 가져와야 하기 때문에 부하가 갈 수 있다.
    // 그래서 redis, hazelcast 같은 메모리 그리드를 써서 세션 처럼 여기에 사용자정보를 올려두고 사용

    // 토큰에는 일반적으로는 인증에 필요한 최소한의 데이터를 넣는다.
    // 비밀번호나 전화번호등을 넣는 것은 안전하지 않다. 공개할 수 있는 정보를 넣자.

    // jwt 는 json web signature 와 json web 인크립션이 있는데
    // 인크립션은 내용을 발행한 서버만 볼수 있도록 암호화 해버리는 것이고
    // 시그니쳐는 어떤 서버든 검증 가능하도록 오픈된 토큰 (base64 로 인코딩되서 오긴 하지만 결국 다 공개되는거와 같음)


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), userService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService);
        http
                // 토큰을 사용하기 위한 설정
                .csrf().disable()
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
                ;

    }
}
// # jwt refresh token 시나리오
// 최초 로그인 시 서버는 Refresh Token과 Access Token을 모두 클라이언트에 발급
// 그리고 Access Token은 DB에 저장하지 않으며, Refresh Token은 DB에 저장한다.
// 토큰을 받은 클라이언트는 로컬 안전한 곳에 Refresh Token을 저장하고, 통신에는 Access Token을 사용한다.
// Access Token 유효기간이 만료되었다는 응답을 서버로부터 받은 클라이언트는 Refresh Token을 꺼내어 같이 재전송하고,
// 서버는 DB에 있는 Refresh Token과 받은 Refresh Token을 대조하여 Access Token 재발행 여부를 결정한다.
// Refresh Token도 만료된 경우에는 재로그인을 해야 한다.
// 따라서 적절한 유효기간을 부여하여 성능을 최적화하고 보안을 유지하는 것이 중요해 보인다.
// 보통 유효기간은 Access Token 1시간, Refresh Token 2주 정도로 잡는다고 한다.