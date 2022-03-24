package com.fc.design.adapter;

public class AirConditioner implements Electronic220V {

    @Override
    public void connect() {
        System.out.println("에어컨 220V On");
    }
}
