package com.example.interceptor.config;

import com.example.interceptor.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor // final 객체를 주입할 생성자 생성
// WebMvcConfgiurer의 구현으로 인해 DispatcherServlet을 간편하게 커스텀할 수 있다.
public class WebConfig implements WebMvcConfigurer { // 웹과 관련된 처리를 하는 WebMvcConfigurer 구현

//  @Autowired -> 순환참조가 발생할 수 있어서 요즘은 RequiredArgsConstructor를 사용한다. (생성자 주입)
    private final AuthInterceptor authInterceptor;

    // Interceptor 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 등록 순서에 따라 실행됨
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/private/*"); // 동작할 url 패턴 지정 가능
    }
}
