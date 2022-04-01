package com.sp.fc.web.config;

import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

//    @Autowired
    // CustomPermissionEvaluator 빈이 만들어질 때 PaperService 도 같이 만들어 져서 생성 시점이 너무 빠르다.
    // 따라서 이 후에 서비스를 감싸는 Proxy가 생성되지 않는 이슈가 발생한다.
    @Lazy // 생성 시점을 늦추기
    private PaperService paperService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        Paper paper = paperService.getPaper((long)targetId);
        if(paper == null) throw new AccessDeniedException("시험지가 존재하지 않음.");


        if(paper.getState() == Paper.State.PREPARE) return false;

        boolean canUse = paper.getStudentIds().stream().filter(userId -> userId.equals(authentication.getName()))
                .findAny().isPresent();

        return canUse;
    }
}
