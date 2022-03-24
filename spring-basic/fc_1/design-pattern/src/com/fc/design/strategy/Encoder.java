package com.fc.design.strategy;

public class Encoder {

    private EncodingStrategy encodingStrategy;

    public void setEncodingStrategy(EncodingStrategy encodingStrategy) {
        this.encodingStrategy = encodingStrategy;
    }

    public String getMessage(String message) {
        return this.encodingStrategy.encode(message);
    }
}