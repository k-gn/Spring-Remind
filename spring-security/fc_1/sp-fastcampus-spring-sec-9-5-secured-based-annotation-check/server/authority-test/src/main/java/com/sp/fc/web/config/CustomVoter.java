package com.sp.fc.web.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public class CustomVoter implements AccessDecisionVoter<MethodInvocation> {

    private String PREFIX = "SCHOOL_";

    @Override // 마킹된 정보를 바탕으로 검사해서 받을지 말지 결정 가능
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute().startsWith(PREFIX);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MethodInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, MethodInvocation object, Collection<ConfigAttribute> attributes) {
        String role = attributes.stream().filter(attr -> attr.getAttribute().startsWith(PREFIX))
                .map(attr -> attr.getAttribute().substring(PREFIX.length())).findAny().get();

        System.out.println(role);
        System.out.println("============================");
        authentication.getAuthorities().stream().forEach(auth -> System.out.println(auth.getAuthority()));
        if(authentication.getAuthorities().stream().filter(auth->auth.getAuthority().equals("ROLE_"+role.toUpperCase()))
        .findAny().isPresent()) {
            return ACCESS_GRANTED;
        }
        return ACCESS_DENIED;
    }
}
