package com.study.springjpa.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest : MVC를 위한 테스트, 컨트롤러가 예상대로 동작하는지 테스트하는데 사용
// WebApplication 관련된 Bean들만 등록(MockMvc)
// 현재 @EnableJpaAuditing이 해당 클래스에 등록되어 있어서 모든 테스트들이 항상 JPA 관련 Bean들을 필요로 하고 있는 상태
// @WebMvcTest같은 슬라이스 테스트는 JPA 관련 Bean들을 로드하지 않기 때문에 에러가 발생
// 처리 방법은 개발자 마다 취향이 다를 수 있다. (지금 방법이 젤 협업에 좋은 것 같음)
@WebMvcTest // @SpringBootTest 어노테이션보다 가볍게 테스트할 수 있음.
@MockBean(JpaMetamodelMappingContext.class) // EnableJpaAuditing을 사용할 경우 설정(jpa 관련 등록)
//@SpringBootTest
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private WebApplicationContext wac;
//    private MockMvc mockMvc;

//    @BeforeEach
//    public void before() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//    }

    @Test
    public void helloTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("hello world!"));
    }
}