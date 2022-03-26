package com.example.springcalculator.component;

import com.example.springcalculator.dto.Req;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.swing.*;


@SpringBootTest // 스프링 테스트 어노테이션, 모든 빈이 등록된다. = 통합테스트
// 필요한 api를 import (add bean)
//@Import({MarketApi.class, DollarCalculator.class})
public class DollarCalculatorTest {

    /*
         Spring Boot Container가 테스트 시에 필요하고, Bean이 Container에 존재한다면 @MockBean을 사용하고 아닌 경우에는 @Mock을 사용
         @MockBean은 스프링 컨텍스트에 mock객체를 등록하게 되고 스프링 컨텍스트에 의해 @Autowired가 동작할 때 등록된 mock객체를 사용할 수 있도록 동작
         spring 에선 bean 으로 관리하고 있기 때문에 Mocking 처리할 객체는 @MockBean 으로 선언
         @Mock은 @InjectMocks에 대해서만 해당 클래스안에서 정의된 객체를 찾아서 의존성을 해결
         @MockBean은 mock 객체를 스프링 컨텍스트에 등록하는 것이기 때문에 @SpringBootTest를 통해서 Autowired에 의존성이 주입

         full auto-configuration 의 annotation 에서는 사용할 수 없다. 오로지 MVC test에만 가능하다.
         예를 들면 @Controller, @ControllerAdvice, WebMvcConfigurer, HandlerMethodArgumentResolver 만이 가능하고
         @Component, @Service or @Repository의 test로는 사용할 수 없다. (@SpringBootTest 를 사용할 경우엔 가능)
         따라서 @Service or @Repository의 test로는 일반 테스트를 이용해야한다.
         스프링이 아니라 일반적인 테스트 이므로 @Autowired를 사용할 수 없다.
         일반테스트에서는 @MockBean, @Autowired를 사용할 수 없으므로, @Mock을 사용한다.
     */

    @MockBean
    private MarketApi marketApi;

    @Autowired
    private Calculator dollarCalculator;

    @Test
    public void dollarCalculatorTest() {
        Mockito.when(marketApi.connect()).thenReturn(3000);
        
        int sum = dollarCalculator.sum(10, 10);

        Assertions.assertEquals(60000, sum);
    }

}
