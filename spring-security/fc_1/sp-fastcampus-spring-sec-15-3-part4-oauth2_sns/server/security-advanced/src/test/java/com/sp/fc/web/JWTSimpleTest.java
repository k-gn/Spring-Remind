package com.sp.fc.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JWTSimpleTest {

    private void printToken(String token){
        String[] tokens = token.split("\\.");
        System.out.println("header: "+new String(Base64.getDecoder().decode(tokens[0])));
        System.out.println("body: "+new String(Base64.getDecoder().decode(tokens[1])));
    }

//    @DisplayName("1. jjwt 를 이용한 토큰 테스트")
//    @Test
//    void test_1(){
////           토큰을 만들땐 builder, 검증할땐 parser 를 사용한다.
//        String okta_token = Jwts.builder().addClaims(
//                Map.of("name", "jongwon", "price", 3000)
//                ).signWith(SignatureAlgorithm.HS256, "jongwon") // 서명 알고리즘과 키값, hs512 가 좀 더 안전하다.
//                .compact(); // 토큰 생성 (base64로 인코딩)
//        System.out.println(okta_token);
//        printToken(okta_token);
//
//        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey("jongwon").parseClaimsJws(okta_token);
//        System.out.println(tokenInfo);
//    }


    @DisplayName("2. java-jwt 를 이용한 토큰 테스트")
    @Test
    void test_2() {
//        java-jwt 에선 키값을 자동으로 해싱 하지 않아서 jjwt 방식과 비교 시 서로 매칭이 안된다. 따라서 직접 해준 후 비교 해야한다.
        byte[] SEC_KEY = DatatypeConverter.parseBase64Binary("jongwon"); // jjwt 해싱 방식
        System.out.println("SEC_KEY : " + SEC_KEY);
//           create 으로 생성, 검증은 require
        String oauth0_token = JWT.create().withClaim("name", "jongwon").withClaim("price", 3000)
                .sign(Algorithm.HMAC256(SEC_KEY));

        System.out.println(oauth0_token);
        printToken(oauth0_token);

        DecodedJWT verified = JWT.require(Algorithm.HMAC256(SEC_KEY)).build().verify(oauth0_token);
        System.out.println(verified.getClaims());

        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey("jongwon").parseClaimsJws(oauth0_token);
        System.out.println(tokenInfo);
    }

    @DisplayName("3. 만료 시간 테스트")
    @Test
    void test_3() throws InterruptedException {
        final Algorithm AL = Algorithm.HMAC256("jongwon"); // 서명 알고리즘 값
        String token = JWT.create().withSubject("a1234") // 대상
                .withNotBefore(new Date(System.currentTimeMillis() + 1000)) // 토큰이 언제부터 유효한지
                .withExpiresAt(new Date(System.currentTimeMillis() + 3000)) // 만료시간
                .sign(AL);

        try {
            DecodedJWT verify = JWT.require(AL).build().verify(token);
            System.out.println(verify.getClaims());
        }catch(Exception ex){
            System.out.println("유효하지 않은 토큰입니다...");
            DecodedJWT decode = JWT.decode(token); // decode 로 토큰을 열어볼 수도 있다.
            System.out.println(decode.getClaims());
        }

    }



}
