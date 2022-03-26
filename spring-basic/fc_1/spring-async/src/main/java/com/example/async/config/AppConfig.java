package com.example.async.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AppConfig {

    // 스레드풀 생성 (빈 등록)
    @Bean("async-thread")
    public Executor asyncThread() {
        // 스레드 풀은 환경과 요청양에 따라 설정이 다르다.
        // 10개가 들어오면 큐에 10개가 차고 큐까지 다차면 증가사이즈만큼 늘어난다 (맥스까지 반복)
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(100); // 최대 크기
        threadPoolTaskExecutor.setCorePoolSize(10); // 증가
        threadPoolTaskExecutor.setQueueCapacity(10); // 시작
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        return threadPoolTaskExecutor;
    }
}
