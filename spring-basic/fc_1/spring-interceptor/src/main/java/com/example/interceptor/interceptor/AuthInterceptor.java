package com.example.interceptor.interceptor;

import com.example.interceptor.annotation.Auth;
import com.example.interceptor.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

// Interceptor
// Filter와 유사한 형태로 존재하지만, 차이점은 Spring Context에 등록된다.
// 따라서 스프링에 모든 Bean에 접근이 가능하다.
// 컨트롤러 전 후에 동작한다.
// 주로 인증 단계 처리나, Logging 하는데 사용한다.
// 어떤 컨트롤러와 매핑되는지도 알 수 있다.
@Slf4j
@Component // 스프링이 관리해주도록 빈 등록
public class AuthInterceptor implements HandlerInterceptor { // HandlerInterceptor 구현

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // filter 단에서 ContentCaching~ 로 형변환해서 doFilter에 담아 보낼 경우 여기서도 형변환해서 사용할 수 있다.
        // handler 객체를 통해 다양한 정보를 알 수 있다. (필터에선 불가능)

        String url = request.getRequestURI();

        // UriComponentsBuilder
        // 여러 개의 파라미터들을 연결하여 URL 형태로 만들어 주는 기능
        // Spring에서 URI와 관련된 작업을 조금 더 쉽게 할 수 있도록 도와주는 클래스
        URI uri = UriComponentsBuilder.fromUriString(url).query(request.getQueryString()).build().toUri();

        log.info("request url : {}", url);
        boolean hasAnnotation = checkAnnotation(handler);
        log.info("has Annotation : {}", hasAnnotation);

        if(hasAnnotation) {
            // 권한 체크(보통 세션이나 쿠키 검증)
            String query = uri.getQuery();
            if(query.equals("name=steve")) { // 특정 name 체크
                return true;
            }
            throw new AuthException();
        }

        return true; // true 면 통과, false 면 동작 x
    }

    private boolean checkAnnotation(Object handler) {

        if(handler instanceof ResourceHttpRequestHandler) { // JS, HTML ...
            return true;
        }

        // has Auth annotation
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handlerMethod.getMethodAnnotation((Class) Auth.class) != null
                || handlerMethod.getBeanType().getAnnotation((Class) Auth.class) != null) {
            return true;
        }

        return false;
    }
}
