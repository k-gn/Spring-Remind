package com.fc.design.adapter;

/*
    - Adapter 패턴은 호환성이 없는 기존클래스의 인터페이스를 변환하여 재사용 할 수 있도록 한다.
    - 개방폐쇄 원칙을 따른다.
 */
public class Main {

    public static void main(String[] args) {

        HairDryer hairDryer = new HairDryer();
        connect(hairDryer);

        Cleaner cleaner = new Cleaner();
        connect(new SocketAdaptor(cleaner));
    }

    public static void connect(Electronic110V electronic110V) {
        electronic110V.powerOn();
    }
}
