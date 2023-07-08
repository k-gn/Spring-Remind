package com.code.design.member;

import com.code.design.EmailSenderService;
import com.code.design.coupon.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberEventHandler {

    private final EmailSenderService emailSenderService;

//    @EventListener
    @TransactionalEventListener // 트랜잭션 커밋 후 동작하는 리스너
    public void memberSignedUpEventListener(MemberSignedUpEvent dto){
        emailSenderService.sendSignUpEmail(dto.getMember());
    }
}
