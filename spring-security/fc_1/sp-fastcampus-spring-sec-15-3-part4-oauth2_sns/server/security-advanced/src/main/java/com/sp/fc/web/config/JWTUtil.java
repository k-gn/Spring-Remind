package com.sp.fc.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sp.fc.user.domain.SpUser;

import java.time.Instant;

public class JWTUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("jongwon");
    private static final long AUTH_TIME = 2;
    private static final long REFRESH_TIME = 60*60*24*7;

    public static String makeAuthToken(SpUser user){
        System.out.println(Instant.now().getEpochSecond()+AUTH_TIME);
        return JWT.create()
                .withSubject(user.getUsername())
                // 자바8 Time API의 Instant 클래스는 시간을 타임스탬프로 다루기 위해서 사용
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME) // 만료시간 = 초단위 현재시간 + AUTH_TIME 으로 설정
                .sign(ALGORITHM);
    }

    public static String makeRefreshToken(SpUser user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static VerifyResult verify(String token){
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder().success(true)
                    .username(verify.getSubject()).build();
        }catch(Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder().success(false)
                    .username(decode.getSubject()).build();
        }
    }

}
