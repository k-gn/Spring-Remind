package com.fc.design.singleton;

public class Main {

    public static void main(String[] args) {

        AClazz aClazz = new AClazz();
        BClazz bClazz = new BClazz();

        System.out.println(aClazz.getSocketClient().equals(bClazz.getSocketClient()));
    }
}
