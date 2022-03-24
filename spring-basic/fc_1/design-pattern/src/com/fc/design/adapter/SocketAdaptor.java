package com.fc.design.adapter;

public class SocketAdaptor implements Electronic110V {

    private Electronic220V electronic220V;

    public SocketAdaptor(Electronic220V electronic220V) {
        this.electronic220V = electronic220V;
    }

    @Override
    public void powerOn() {
        electronic220V.connect();
    }
}
