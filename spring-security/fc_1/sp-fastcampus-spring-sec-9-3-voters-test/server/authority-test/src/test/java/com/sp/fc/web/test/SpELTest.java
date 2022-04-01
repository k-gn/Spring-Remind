package com.sp.fc.web.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.junit.jupiter.api.Assertions.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Person {
    private String name;
    private int height;

    public boolean over(int pivot){
        return height >= pivot;
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Horse {
    private String name;
    private int height;

    public boolean over(int pivot){
        return height >= pivot;
    }
}

public class SpELTest {

    ExpressionParser parser = new SpelExpressionParser();
    Person person = Person.builder()
            .name("홍길동").height(178).build();

    Horse nancy = Horse.builder().name("nancy").height(160).build();

    @DisplayName("4. Context 테스트 ")
    @Test
    void test_4() {

        // EvaluationContext 설정
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setBeanResolver(new BeanResolver() { // bean name 으로 bean 을 찾아준다.
            @Override
            public Object resolve(EvaluationContext context, String beanName) throws AccessException {
                return beanName.equals("person") ? person : nancy;
            }
        });
        ctx.setRootObject(person);
        ctx.setVariable("horse", nancy);

        // # EvaluationContext 를 통해 값 다루기 ( 이 방법이 SpEL )
        // Root 객체 접근
        assertTrue(parser.parseExpression("over(170)").getValue(ctx,
                Boolean.class));

        // Variable 접근
        assertFalse(parser.parseExpression("#horse.over(170)").getValue(ctx,
                Boolean.class));

        // Bean 접근
        assertTrue(parser.parseExpression("@person.over(170)").getValue(ctx,
                Boolean.class));
        assertFalse(parser.parseExpression("@nancy.over(170)").getValue(ctx,
                Boolean.class));

    }

    @DisplayName("3. 메소드 호출")
    @Test
    void test_3() {
        assertTrue(parser.parseExpression("over(170)").getValue(person,
                Boolean.class));
        assertFalse(parser.parseExpression("over(170)").getValue(nancy,
                Boolean.class));
    }


    @DisplayName("2. 값 변경")
    @Test
    void test_2() {
        parser.parseExpression("name").setValue(person, "호나우드");
        assertEquals("호나우드", parser.parseExpression("name")
                .getValue(person, String.class));
    }

    @DisplayName("1. 기본 테스트(값 가져오기) ")
    @Test
    void test_1(){
        assertEquals("홍길동", parser.parseExpression("name")
                .getValue(person, String.class));
    }

    // 스프링 시큐리티에서 SpEL 은 중요하게 쓰이고 있다.
    // WebExpressionVoter 와 PreInvocationAuthorizationAdviceVoter 가 SpEL 를 사용해서 시큐리티를 평가한다.
    // SecurityExpressionRoot 를 상속 받은 WebSecurityExpressionRoot 와 MethodSecurityExpressionRoot 를
    // 각각 WebExpressionVoter 와 PreInvocationAuthorizationAdviceVoter 가 Context Root 로 보고 있다.
    // BeanResolver 는 기본적으로 ApplicationContext BeanResolver

}
