package com.fc.design.decorator;

/*
    - 데코레이터 패턴은 기존 뼈대는 유지하되, 이후 필요한 형태로 꾸밀 때 사용한다.
    - 확장이 필요한 경우 상속의 대안으로도 활용한다.
 */
public class Main {

    public static void main(String[] args) {

        ICar audi = new Audi(1000);
        audi.showPrice();

        ICar a3 = new A3(audi, "A3");
        a3.showPrice();
    }
}
