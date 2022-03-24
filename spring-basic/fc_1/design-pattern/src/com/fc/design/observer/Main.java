package com.fc.design.observer;

/*
    - Observer 패턴은 변화가 일어났을 때, 미리 등록된 다른 클래스에 통보해주는 패턴
    - 이벤트 리스너가 가장 대표적인 예다.
 */
public class Main {

    public static void main(String[] args) {

        Button button = new Button("Button");
        button.addListener(System.out::println);
        button.click("메시지 전달 : click");
    }
}
