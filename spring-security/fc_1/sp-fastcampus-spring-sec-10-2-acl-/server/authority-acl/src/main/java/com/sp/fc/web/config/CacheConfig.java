package com.sp.fc.web.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ResourceBundle;

//@Configuration
//@EnableCaching // 캐시에 대한 설정, 근데 기본적으로 아무 설정 안하면 스프링이 캐시를 제공
public class CacheConfig {

    // EhCache 로 스프링 캐시를 대체하여 사용
//    @Bean
//    public EhCacheCacheManager cacheManager(){
//        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
//        cacheManager.setCacheManager(ehcacheFactoryBean().getObject());
//        return cacheManager;
//    }
//
//    @Bean
//    public EhCacheManagerFactoryBean ehcacheFactoryBean() {
//        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
//        return factoryBean;
//    }

}
