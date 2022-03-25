package com.example.springcore.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
     Target의 기능은 어노테이션을 붙일 수 있는 대상을 지정하는 것이다.
     CONSTRUCTOR / METHOD / FIELD 3 가지는 이름 그대로
     생성자와 메소드 필드에 어노테이션을 붙일 수 있다는 의미이며,
     TYPE 는 클래스,인터페이스,열거타입에 어노테이션을 붙일 수 있다는 의미이다.

     Retention : 어노테이션 타입을 어디까지 보유할지를 설정하는 것
     - SOURCE : 어노테이션을 사실상 주석처럼 사용하는 것. 컴파일러가 컴파일할때 해당 어노테이션의 메모리를 버린다.
     - CLASS : 컴파일러가 컴파일에서는 어노테이션의 메모리를 가져가지만 실질적으로 런타임시에는 사라지게 된다.
               런타임시에 사라진다는 것은 리플렉션으로 선언된 어노테이션 데이터를 가져올 수 없게 됩니다. (default).
     - RUNTIME : 어노테이션을 런타임시에까지 사용할 수 있다. JVM이 자바 바이트코드가 담긴 class 파일에서 런타임환경을 구성하고
                 런타임을 종료할 때까지 메모리는 살아있다..
 */
@Target({ElementType.TYPE, ElementType.METHOD}) // 사용가능한 지점
@Retention(RetentionPolicy.RUNTIME) // 어느 시점까지 어노테이션의 메모리를 가져갈 지 설정
public @interface Timer {
}
