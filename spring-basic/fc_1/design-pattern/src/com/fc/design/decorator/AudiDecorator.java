package com.fc.design.decorator;

// Decorator Pattern
// 기존 뼈대는 유지하되 필요한 형태로 꾸밀 때 사용
// 상속의 대안으로 활용 (확장)
// 개방 폐쇠와 의존 역전을 따른다.
public class AudiDecorator implements ICar {

    protected ICar audi;
    protected String modelName;
    protected int modelPrice;

    public AudiDecorator(ICar audi, String modelName, int modelPrice) {
        this.audi = audi;
        this.modelName = modelName;
        this.modelPrice = modelPrice;
    }

    @Override
    public int getPrice() {
        return audi.getPrice() + modelPrice;
    }

    @Override
    public void showPrice() {
        System.out.println(modelName + "의 가격 : " + getPrice());
    }
}