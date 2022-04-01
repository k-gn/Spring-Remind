package com.sp.fc.web.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SecurityMessageService {


    // 기본적으로 권한 어노테이션은 PreInvocationAuthorizationAdviceVoter 가 투표 
//    @PreAuthorize("hasRole('USER')")
    @PreAuthorize("@nameCheck.check(#name)")
    public String message(String name){
        return name;
    }

}
