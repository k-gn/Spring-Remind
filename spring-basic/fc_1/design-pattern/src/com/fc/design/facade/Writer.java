package com.fc.design.facade;

public class Writer {

    private String fileName;

    public Writer(String fileName) {
        this.fileName = fileName;
    }

    public void fileConnect() {
        String msg = String.format("Writer %s 로 연결합니다.", fileName);
        System.out.println(msg);
    }

    public void write() {
        System.out.println("Writer write");
    }

    public void fileDisConnect() {
        System.out.println("Writer fileDisConnect");
    }
}
