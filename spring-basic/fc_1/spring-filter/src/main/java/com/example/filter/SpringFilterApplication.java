package com.example.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // @WebFilter 어노테이션을 읽는다.
public class SpringFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFilterApplication.class, args);
    }

}
