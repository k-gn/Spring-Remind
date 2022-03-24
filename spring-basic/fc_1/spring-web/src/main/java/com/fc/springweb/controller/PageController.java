package com.fc.springweb.controller;

import com.fc.springweb.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // page 를 리턴하는 컨트롤러 (자동으로 resources에 있는 html 파일을 찾아 리턴)
public class PageController {

    @RequestMapping("/main")
    public String main() {
        return "main.html";
    }

    // Page Controller 에서 Json 데이터 내려주는 방법 (하지만 페이지 컨트롤러에선 이렇게 데이터를 리턴하는건 비추)
    //1. ResponseEntity
    //2. ResponseBody
    @ResponseBody
    @GetMapping("/user")
    public User user() {
        // var : 타입 추론
        var user = new User();
        user.setName("steve");
        user.setAddress("패스트 캠퍼스");
        return user;
    }

    // 반대로 Rest Controller 에서 페이지 내려주는 방법
    // -> ModelAndView 사용 시 뷰 리졸버 동작

    // @ModelAttribute 메소드 실행 결과로 리턴된 객체는 자동으로 Model에 저장
}
