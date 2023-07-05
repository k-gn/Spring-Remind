package com.code.design.exception;

import com.code.design.ErrorCode;

/*
    # 최상위 내부 비즈니스 예외 클래스
    - 통일감 있는 예외 처리
    - 하나의 예외 핸들러로 처리 가능
 */
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
