package com.fc.design.facade;

public class Reader {

    private String fileName;

    public Reader(String fileName) {
        this.fileName = fileName;
    }

    public void fileConnect() {
        String msg = String.format("Reader %s 로 연결합니다.", fileName);
        System.out.println(msg);
    }

    public void fileRead() {
        System.out.println("Reader fileRead");
    }

    public void fileDisConnect() {
        System.out.println("Reader fileDisConnect");
    }
}