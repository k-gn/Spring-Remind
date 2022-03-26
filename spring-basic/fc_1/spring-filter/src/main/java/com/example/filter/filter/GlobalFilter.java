package com.example.filter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Filter
// Web Application 에서 관리된다.
// spring context 영역에 해당 x - Dispathcer Servlet 앞단에서 가장 먼저 통과하는 곳
// Client 로 부터 오는 요청/응답에 대해 최초/최종 단계의 위치한다.
// 요청/응답의 정보를 변경하거나 순수한 정보를 확인할 수 있다.
// request, response의 Logging 용도나 인증관련 Logic 등을 처리할 수 있다.
// 선/후 처리 함으로써 business logic과 분리시킨다.
// aop 같은건 객체로 매핑되서 들어오지만 filter는 가장 앞단에서 스프링에 의해 데이터가 변환되기 전의 순수한 request 를 다룬다. (spring context에 등록x)
// 인증, 보안 처리나 (세션검증 등) 로깅 처리 등을 할 수 있다.
@Slf4j
@WebFilter("/apif/user/*") // 해당 요청url 에서만 필터 동작
//@Component // 빈 등록해서 필터 등록, 따로 설정파일에서 등록할수도 있다.
public class GlobalFilter implements Filter { // Filter 구현

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("Filter !!!!!");
        // 전처리
        // Reader로 read를 한번 하게 되면 안에 내용을 다 읽게 되고 그 이후엔 더 이상 읽을 수 없게 된다. (커서가 맨 끝에 가있음)
        // 누구든 read를 한번 하게 되면 내용을 이 후에 더 이상 읽을 수 없다.
        // 이때 스프링에서 캐시를 이용하면 몇 번이든 읽을 수 있다.
        // ContentCaching ~ 클래스를 사용
        // 단. 후처리(chain.doFilter 이 후) 때 내용을 읽을 수 있음 (전처리에선 길이만 읽어온다.)
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest)request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse)response);

        String prevUrl = httpServletRequest.getRequestURI();
        String prevRequestContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request url : {}, requestBody : {}", prevUrl, prevRequestContent);
        log.info("===================================================");

        // 주의할 점은 전처리때 사실 request 내용을 담은게 아닌 길이만 지정한다.
        // doFilter를 통해 내부 스프링으로 들어가야 request content 가 ByteArray에 담긴다.
        chain.doFilter(httpServletRequest, httpServletResponse); // 기준

        // 후처리
        String postUrl = httpServletRequest.getRequestURI();
        String postRequestContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request url : {}, requestBody : {}", postUrl, postRequestContent);

        //여기서 응답할 내용을 다 빼버리기 때문에 밑에 copyBodyToResponse() 사용하여 읽은 내용만큼 다시 복사를 해준다
        String responseContent = new String(httpServletResponse.getContentAsByteArray());
        int httpStatus = httpServletResponse.getStatus();

        httpServletResponse.copyBodyToResponse();

        log.info("response status : {}, responseBody : {}", httpStatus, responseContent);
    }
}
