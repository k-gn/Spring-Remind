package com.sp.fc.web.config;

import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PaperService paperService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    // # hasPermission
    // target : 대상 객체를 보고 permission을 검사합니다.
    // targetId, type : type을 보고 targetId인 대상 객체를 조회해와서 permission을 검사합니다.

    // PermissionEvaluator
    // 이제까지의 권한 검사는 Authentication 통행증을 검사했다.
    // 그런데, 심사를 하려면 통행증만 검사하면 안되고, 가져가려는 물건이 무엇인지를 같이 봐야 하는 경우가 많다.
    // 이런 것들을 단순 Voter 들은 평가할 수 없다.
    // 따라서 PermissionEvaluator 를 사용하거나 객체 별로 접근 권한을 DB로 관리해주는 ACL 처럼 권한을 체크하기 위한 별도의 설계가 들어가야 한다.

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, // 타겟 식별 정보
                                 String targetType, // 해당 객체 타입 정보
                                 Object permission) { // 해당 객체 허가 정보
        Paper paper = paperService.getPaper((long)targetId);
        System.out.println("targetId : " + targetId);
        System.out.println("targetType : " + targetType);
        System.out.println("permission : " + permission);
        if(paper == null) throw new AccessDeniedException("시험지가 존재하지 않음.");


        if(paper.getState() == Paper.State.PREPARE) return false;

        boolean canUse = paper.getStudentIds().stream().filter(userId -> userId.equals(authentication.getName()))
                .findAny().isPresent();

        return canUse;
    }
}
