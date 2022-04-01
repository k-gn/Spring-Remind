package com.sp.fc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication // 모듈에서 테스트하기 위해 지정
//public class PaperUserApp {
//
//    public static void main(String[] args) {
//        SpringApplication.run(PaperUserApp.class, args);
//    }

    // 내부에 설정 클래스 지정 ( 실행 시 TEST 쪽에선 PaperUserModule 이 동작안해서 따로 테스트쪽에 별도로 다시 지정 )
//    @Configuration
//    @ComponentScan("com.sp.fc.user")
//    @EnableJpaRepositories(basePackages = {
//            "com.sp.fc.user.repository"
//    })
//    @EntityScan(basePackages = {
//            "com.sp.fc.user.domain"
//    })
//    class Config {
//
//    }

//}
