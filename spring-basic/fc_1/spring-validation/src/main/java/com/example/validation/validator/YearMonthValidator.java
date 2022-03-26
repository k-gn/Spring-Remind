package com.example.validation.validator;

import com.example.validation.annotation.YearMonth;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// 검증용 클래스
// custom validation
public class YearMonthValidator implements ConstraintValidator<YearMonth, String> { // custom 어노테이션과 확인할 값을 지정

    private String pattern;

    @Override
    public void initialize(YearMonth constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            // LocalDate은 기본적으로 dd까지 들어가야 한다.
            LocalDate localDate = LocalDate.parse(value + "01", DateTimeFormatter.ofPattern(this.pattern));
        }catch (Exception e) {
            return false;
        }
        return true;
    }
}