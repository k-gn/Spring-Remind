package com.sp.fc.web.controller;


import com.sp.fc.web.config.CustomSecurityTag;
import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
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

//    @PreAuthorize("isStudent()")
    @GetMapping("/mypapers")
    public List<Paper> myPapers(@AuthenticationPrincipal User user){
        return paperService.getMyPapers(user.getUsername());
    }

    // RUN_AS 만 있으면 에러 난다. 최소한 넘어갈 수 있는 권한이 하나이상 같이 있어야 된다.
    // 컨트롤러 단에서 통과 후 통과한 사용자에 권한에 RUN_AS 앞에 ROLE_ 가 붙은 권한이 추가된다. (컨트롤러 단에서 임시권한 부여)
    @Secured({"ROLE_USER", "RUN_AS_PRIMARY"}) // 임시 권한은 RUN_AS 로 시작
    @GetMapping("/allpapers")
    public List<Paper> allpapers(@AuthenticationPrincipal User user){
        return paperService.getAllPapers();
    }

    // 직접 만든 어노테이션을 CustomMetadataSource 를 통해서 등록해 사용
    @CustomSecurityTag("SCHOOL_PRIMARY")
    @GetMapping("/getPapersByPrimary")
    public List<Paper> getPapersByPrimary(@AuthenticationPrincipal User user){
        return paperService.getAllPapers();
    }

//    @PreAuthorize("hasPermission(#paperId, 'paper', 'read')")
    @PostAuthorize("returnObject.studentIds.contains(principal.username)")
    @GetMapping("/get/{paperId}")
    public Paper getPaper(@AuthenticationPrincipal User user, @PathVariable Long paperId){
        return paperService.getPaper(paperId);
    }

    // annotation -> securityInterceptor -> metadataSource -> manager -> voter

}
