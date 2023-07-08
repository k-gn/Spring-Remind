package com.code.design.part2;

public class OrderLegacy {

    private long id;
    // KAKAO, SMS, EMAIL 등 메세지 플랫폼등이 있음
    private String messageTypes; // "KAKAO,SMS,EMAIL" 형태로 저장

    public OrderLegacy(String messageTypes) {
        this.messageTypes = messageTypes;
    }

    // 주문 메시지는 Order 클래스의 책임인가?
    public String[] getMessageTypes() {
        return messageTypes.split(",");
    }
}
