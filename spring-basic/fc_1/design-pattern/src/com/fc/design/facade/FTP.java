package com.fc.design.facade;

public class FTP {

    private String host;
    private int port;
    private String path;

    public FTP(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public void connect() {
        System.out.println("FTP Connect");
    }

    public void moveDirectory() {
        System.out.println("FTP moveDirectory");
    }

    public void disConnect() {
        System.out.println("FTP disConnect");
    }
}
