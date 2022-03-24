package com.fc.design.proxy;

import com.fc.design.proxy.aop.AopBrowser;
import com.fc.design.proxy.cache.Browser;
import com.fc.design.proxy.cache.BrowserProxy;
import com.fc.design.proxy.cache.IBrowser;

import java.util.concurrent.atomic.AtomicLong;

/*
    - Proxy 는 대리인이라는 뜻으로써, 뭔가를 대신해서 처리하는 것
    - Proxy Class 를 통해서 대신 전달하는 형태로 설계되며, 실제 Client 는 Proxy 로부터 결과를 받는다
    - Cache 기능으로 활용 가능
    - 스프링의 aop 기능은 프록시 패턴을 통해 제공된다.
    - 개방폐쇄 원칙과 의존역전 원칙을 따른다.
 */
public class Main {

    public static void main(String[] args) {

        Browser browser = new Browser("www.naver.com");
        browser.show();

        IBrowser browserProxy = new BrowserProxy(browser);
        browserProxy.show();
        browserProxy.show();

        AtomicLong start = new AtomicLong(); // 동시성 문제 해결
        AtomicLong end = new AtomicLong();
        IBrowser aopBrowser = new AopBrowser("www.naver.com",
                () -> {
                    System.out.println("before");
                    start.set(System.currentTimeMillis());
                },
                () -> {
                    System.out.println("after");
                    end.set(System.currentTimeMillis());
                });
        aopBrowser.show();
        System.out.println("loading time : " + (end.get() - start.get()));
        aopBrowser.show();
        System.out.println("loading time : " + (end.get() - start.get()));
    }
}
