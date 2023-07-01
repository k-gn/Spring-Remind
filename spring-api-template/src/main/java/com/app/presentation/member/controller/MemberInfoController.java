package com.app.presentation.member.controller;

import com.app.presentation.member.controller.response.MemberInfoResponse;
import com.app.application.member.service.MemberInfoService;
import com.app.global.resolver.member.TokenMember;
import com.app.global.resolver.member.MemberInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@TokenMember MemberInfo memberInfo) {

        Long memberId = memberInfo.getMemberId();
        MemberInfoResponse memberInfoResponse = memberInfoService.getMemberInfo(memberId);

        return ResponseEntity.ok(memberInfoResponse);
    }

}
