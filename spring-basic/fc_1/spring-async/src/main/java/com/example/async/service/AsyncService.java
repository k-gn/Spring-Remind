package com.example.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncService {

    // CompletableFuture : 다른 스레드에서 실행시키고 결과를 반환받는다.
    // 보통 한번에 여러 api 를 작동시키고 결과를 join 해서 리턴할 때 사용하는게 좋다.
    // aop proxy 기반으로 public 에만 지정할 수 있다.
    @Async("async-thread") // 스레드 풀을 지정해줄 수도 있다.
    public CompletableFuture run() {
//        hello(); (x) -> 같은 클래스 내에 존재하는 메소드를 그냥 호출 시 동작하지 않는다.
        return new AsyncResult(hello()).completable();
    }
    // 이렇게 쓰는것 보다 한 메소드에 CompletableFuture를 여러개 동작 시키고 합쳐서 결과를 응답하는 형태가 더 좋다.

    // 비동기 처리
    public String hello() {
        for(int i=0; i<10; i++) {
            try {
                Thread.sleep(2000);
                log.info("thread sleep...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "async hello";
    }
}
