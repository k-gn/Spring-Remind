package com.sp.fc.web.controller;


import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/paper")
@RestController
public class PaperController {

    @Autowired
    private PaperService paperService;

    // # 메소드 후처리
    // AfterInvocationManager 가 동작한다.
    // AfterInvocationManager (AfterInvocationProviderManager) -> AfterInvocationProvider (PostInvocationAdviceProvider)
    // AfterInvocationProvider 를 차례로 거친 후 결과를 리턴
    // PostInvocationAttribute configAttribute 를 가지고 있다.

    // 메소드가 결과 리턴 후 리턴된 객체에 접근할 수 있는지 체크
    // 리턴된 객체를 체크할 때 사용되는 어노테이션이 PostAuthorize, PostFilter
    // 보통, 어떤 객체의 값을 변경해야 하는 경우에는 메소드에 들어오기 전에, 값을 조회하려고 하는 경우에는 값을 가져온 이후에 각각 접근 권한을 체크한다.
    // 이 때, AfterInvocationProvider 가 어노테이션이 된 메소드를 빠져나갈 때 returnedObject 체크 후 리턴

    // AuthenticatedVoter 나 RoleVoter 같은 Secured 기반 애들은 SecurityConfig Attribute 를 가지고 있다.
    // SecurityConfig 는 추가로 voter 를 구현해서 붙여줄 수 있다.

    // 어노테이션이나 config 에서 설정한 값들은 보통 configAttribute 의 metadataSource 의 hashMap 형태로 기록되어 있다.
    // 필터 쪽은 WebExpressionConfigAtrribute
    // 메소드쪽은 PreInvocationAttribute, PostInvocationAttribute 를 가지고 있다.
    // 이 주요 3가지 configAttribute는 모두 Expression 기반이다.
    
    // MethodSecurityInterceptor 의 중요한 3가지 멤버
    // - AccessDecisionManager : @Secured 나 @PreAuthorize, @PreFilter 를 처리  (voter 기반)
    // - AfterInvocationManager : @PostAuthorize, @PostFilter 를 처리 (voter 기반 x)
    // - RunAsManager : 임시권한 부여

//    @PreAuthorize("isStudent()")
//    @PostFilter("filterObject.state != T(com.sp.fc.web.service.Paper.State).PREPARE") // Enumeration 은 T 함수를 거쳐서 가져올 수 있다.
    // filterObject에 List 안에있는 값 한개씩 들어감
//    @PostFilter("notPrepareSate(filterObject) && filterObject.studentIds.contains(#user.username)")
    @GetMapping("/mypapers")
    public List<Paper> myPapers(@AuthenticationPrincipal User user){
        System.out.println("myPapers ----------------------------");
        System.out.println(paperService.getMyPapers(user.getUsername()));
        return paperService.getMyPapers(user.getUsername());
    }

//    @PreAuthorize("hasPermission(#paperId, 'paper', 'read')")
//    @PostAuthorize("returnObject.studentIds.contains(#user.username)")
    // returnObject : 리턴값이 들어간다.
    @PostAuthorize("returnObject.studentIds.contains(principal.username)")
    @GetMapping("/get/{paperId}")
    public Paper getPaper(@AuthenticationPrincipal User user, @PathVariable Long paperId){
        return paperService.getPaper(paperId);
    }

    // PreInvocation 이든 PostInvocation 이든 둘다 MethodSecurityExpressionRoot 를 같이 사용한다.
    // 따라서 둘다 상황에 맞게 filterObject, returnObject 사용 가능

}

// 옥탑방 개발자 사이트 그림들 참고!!!