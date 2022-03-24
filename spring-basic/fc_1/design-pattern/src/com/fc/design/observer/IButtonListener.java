package com.fc.design.observer;

// Observer Pattern
// 변화가 일어날 때 미리 등록된 다른 클래스에 통보, 전달해주는 패턴
// ex. event Listener
public interface IButtonListener {
    void clickEvent(String event);
}