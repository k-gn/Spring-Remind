package com.fc.design.proxy.cache;

public class Browser implements IBrowser {

    private String url;

    public Browser(String url) {
        this.url = url;
    }

    @Override
    public Html show() {
        System.out.println("browser loading from : " + url);
        return new Html(url);
    }

    public String getUrl() {
        return url;
    }
}
