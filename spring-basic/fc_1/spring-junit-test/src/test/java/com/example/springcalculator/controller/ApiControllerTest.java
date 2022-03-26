package com.example.springcalculator.controller;

import com.example.springcalculator.component.Calculator;
import com.example.springcalculator.component.DollarCalculator;
import com.example.springcalculator.component.MarketApi;
import com.example.springcalculator.dto.Req;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


// Web Test 어노테이션 (MVC를 위한 테스트, 컨트롤러가 예상대로 동작하는지 테스트하는데 사용)
// @SpringBootTest 을 같이 사용할 수 없다.
// 웹에서 테스트하기 힘든 컨트롤러를 테스트하는 데 적합.
// 웹상에서 요청과 응답에 대해 테스트할 수 있음.
// Test 할 controller 지정 가능, 필요한 것들만 loading 시킨다. (자원 낭비 방지)
@WebMvcTest(ApiController.class)
@AutoConfigureWebMvc // MVC와 관련된 Bean을 올린다
@Import({Calculator.class, DollarCalculator.class})
public class ApiControllerTest {

    @MockBean
    private MarketApi marketApi;

    // MVC를 Mocking으로 테스트할 객체
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach // 테스트 전에 실행할 메소드
    public void init() {
        Mockito.when(marketApi.connect()).thenReturn(3000);
    }

    @Test
    public void sumTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("http://localhost:8080/api/sum")
                .queryParam("x", "10")
                .queryParam("y", "10")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("60000")
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void minusTest() throws Exception {

        Req req = new Req();
        req.setX(10);
        req.setY(10);

        String json = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(
                MockMvcRequestBuilders.post("http://localhost:8080/api/minus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                // jsonpath 에 내가 기대하는 result 의 value 값을 검증
                MockMvcResultMatchers.jsonPath("$.result").value("0")
        ).andExpect(
                // jsonpath 에 내가 기대하는 값의 value 값을 검증
                MockMvcResultMatchers.jsonPath("$.response.resultCode").value("OK")
        ).andDo(MockMvcResultHandlers.print());
    }
}
// jacoco 로 테스트 커버리지 확인 하기
// 내가 만든 코드가 어디까지 커버하고 있는지 확인 가능
// gradle 에 id 'jacoco' 추가
// Task -> verification 에서 test 실행 후 jacoco~ 실행
// build 폴더에 reports 에서 jacoco report 확인 가능(index.html)