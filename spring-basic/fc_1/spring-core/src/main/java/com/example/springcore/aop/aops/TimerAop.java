package com.example.springcore.aop.aops;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimerAop {

    @Pointcut("execution(* com.example.springcore.aop.controller..*.*(..))")
    public void cut(){}

    @Pointcut("@annotation(com.example.springcore.aop.annotation.Timer)")
    private void enableTimer() {}

    @Around("cut() && enableTimer()")
    public void arround(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch(); // 시간을 재기위한 StopWatch 클래스
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        System.out.println("total time : " + stopWatch.getTotalTimeSeconds());
    }
}