package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.sp.fc.config", // 여길 스캔하려고 들어갈 때 안에 모듈 설정파일을 읽고 한번더 스캔을 해줌
        "com.sp.fc.web"
})
public class RememberMeTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RememberMeTestApplication.class, args);
    }

}
