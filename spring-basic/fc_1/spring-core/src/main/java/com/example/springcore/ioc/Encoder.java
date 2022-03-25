package com.example.springcore.ioc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

// IOC : 개발자가 아닌 스프링 컨텍스트가 객체의 생명 주기를 직접 관리한다.
// DI : 필요한 의존성을 스프링이 알아서 주입해준다, 테스트가 용이, 낮은 결합도, 순환참조 방지 가능
@Component
public class Encoder implements IEncoder {

    private IEncoder iEncoder;

    public Encoder(@Qualifier("base64Encoder") IEncoder iEncoder) {
        this.iEncoder = iEncoder;
    }

//    public Encoder(IEncoder iEncoder) {
//        this.iEncoder = iEncoder;
//    }

    public void setIEncoder(IEncoder iEncoder) {
        this.iEncoder = iEncoder;
    }

    public String encode(String message) {
        return iEncoder.encode(message);
    }
}
