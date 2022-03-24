package com.fc.design.proxy.cache;

import java.util.HashSet;
import java.util.Set;

public class BrowserProxy implements IBrowser {

    private Browser browser;

    private Html html;

    private Set<String> urls;

    public BrowserProxy(Browser browser) {
        this.browser = browser;
        this.urls = new HashSet<>();
    }

    @Override
    public Html show() {
        if(!urls.contains(browser.getUrl())) {
            this.html = new Html(browser.getUrl());
            urls.add(browser.getUrl());
            System.out.println("BrowserProxy loading html from : " + browser.getUrl());
        }else {
            System.out.println("BrowserProxy use cache html : " + browser.getUrl());
        }
        return html;
    }
}
