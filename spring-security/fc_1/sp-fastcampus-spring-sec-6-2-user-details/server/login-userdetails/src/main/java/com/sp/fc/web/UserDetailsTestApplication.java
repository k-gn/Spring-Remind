package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// 스캔 범위 설정
// (모듈로 사용하고 있어서 스캔 지정 필요한 듯 -> 기본적으로 빈은 빈끼리 주입이 가능하기 때문)
@SpringBootApplication(scanBasePackages = {
        "com.sp.fc.user",
        "com.sp.fc.web"
})
@EntityScan(basePackages = {
        "com.sp.fc.user.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.sp.fc.user.repository"
})
public class UserDetailsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDetailsTestApplication.class, args);
    }

}
