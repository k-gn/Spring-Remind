package com.sp.fc.web.controller;


import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

//    @PreAuthorize("hasPermission(#paperId, 'paper', 'read')") // hasPermission 보단 PostAuthorize 가 자연스럽다.
    @PostAuthorize("returnObject.studentIds.contains(principal.username)")
    @GetMapping("/get/{paperId}")
    public Paper getPaper(@AuthenticationPrincipal User user, @PathVariable Long paperId){
        return paperService.getPaper(paperId);
    }

    // MetadataSource 안에는 어노테이션으로 권한 마킹을 해놓은 정보(configAttribute)들이 있다. (애네를 사용해 권한체크를 한다)
    // FilterInvocation 쪽은 Expression 기반이기 때문에 MetadataSource 확장 가능성이 적어서 secured 기반은 MethodInvocation 쪽에서 일어나는 듯
    // AbstractSecurityInterceptor 안에서 MetadataSource 를 호출하여 configAttribute 를 추출
    // MethodSecurityMetadataSource 에서 Delegating 은 MetadataSource 를 List 로 가지고 있다가 차례대로 적절한 MetadataSource 를 고른 후
    // 처리할 수 있는 MetadataSource 에 대해서 configAttribute 컬렉션을 뽑아준다. (= 중재, 관리자 역할)
    // 실질적으로 MetadataSource 는 PrePost or Secured
    // 만약 새로운 권한 어노테이션을 추가하고 싶다면 MetadataSource 를 만들어서 추가해주는 작업이 필요하다.
    // GlobalMethodSecurityConfiguration 에서 methodMetadataSource 를 만든다.
    // PrePost 와 Secured 가 동시에 선언되 있다면 Secured 는 무시된다. (PrePost 에서 먼저 메소드에 대한 configAttribute 반환)

    // Custom 한 권한적용이 필요하다면 어떻게? -> Secured
    // @Secured 기반의 권한 체크는 메소드의 사전에만 체크한다.
    // 사후 체크를 하려면 @PostAuthorize(혹은 @PostFilter) 를 사용하거나 별도의 AOP 를 설계해야 한다.
    // @Secured 는 또한 Voter 를 추가할 수 있도록 설계되었다.
    // Expression 기반의 권한 체크가 편리하고 권장되기는 하지만,
    // 기존에 구축된 Security 들 중에 일부는 @Secured 기반으로 구축이 되어
    // 있을 수 있고, 그 기반에서 소스를 유지보수 혹은 확장해 나가야 할 수도 있다.

    @Secured({"SCHOOL_PRIMARY"})
    @GetMapping("/papersByPrimary")
    public List<Paper> papersByPrimary(){
        return paperService.getAllPapers();
    }
}
