package com.code.design.validation;

import com.code.design.member.MemberRepository;
import java.text.MessageFormat;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailDuplicationValidator implements ConstraintValidator<EmailUnique, String> {

    private final MemberRepository memberRepository;

    @Override
    public void initialize(EmailUnique emailUnique) {

    }

    // isValid() 가 핵심 구현
    @Override
    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        boolean isExistEmail = memberRepository.existsByEmail(email);
        if (isExistEmail) {
            cxt.disableDefaultConstraintViolation(); // 기본 메시지 제거
            cxt.buildConstraintViolationWithTemplate( // 새로운 메시지 추가
                    MessageFormat.format("{0} already exists!", email))
                .addConstraintViolation();
        }
        return !isExistEmail;
    }
}