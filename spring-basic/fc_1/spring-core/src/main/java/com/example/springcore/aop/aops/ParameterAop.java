package com.example.springcore.aop.aops;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect // aop로 동작하기 위한 어노테이션션
@Component
public class ParameterAop {

    // 접근제한자 패키지명 클래스 메소드 인자
    // .. : 모든 하위패키지, 모든 인자, * : 모든 클래스 또는 메소드
    @Pointcut("execution(* com.example.springcore.aop.controller..*.*(..))") // aop를 적용할 지점
    private void cut() {}

    @Before("cut()")
    public void before(JoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println(method.getName());

        System.out.println("Before");
        Object[] args = joinPoint.getArgs();
        for(Object obj : args) {
            System.out.println(obj.getClass().getSimpleName()); // 클래스명
            System.out.println(obj); // 값
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturn(JoinPoint joinPoint, Object returnObj) {
        System.out.println("AfterReturn");
        System.out.println(returnObj);
    }
}