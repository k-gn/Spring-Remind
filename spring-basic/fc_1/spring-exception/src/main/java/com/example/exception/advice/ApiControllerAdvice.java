package com.example.exception.advice;

import com.example.exception.controller.ApiController;
import com.example.exception.dto.Error;
import com.example.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// 글로벌하게 예외처리를 해주는 클래스
// 범위를 제한할 수도 있다.
@RestControllerAdvice(basePackageClasses = ApiController.class) // json 형태로 결과 반환 (RestController + ControllerAdvice)
//@ControllerAdvice // 뷰 리졸버로 결과 반환
public class ApiControllerAdvice {

    // 모든 예외를 다잡는다.
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e) {
        System.out.println("exception");
        System.out.println(e.getClass().getName());
        System.out.println(e.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }

    // ModelAttribute 일 때 @Valid의 경우 바인딩이 실패가되면 BindingException 을 발생
    
    // 특정 예외를 잡는다.
    // @Valid의 경우 바인딩이 실패가되면 MethodArgumentNotValidException 을 발생
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        System.out.println("MethodArgumentNotValidException");
        List<Error> errorList = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {
            FieldError field = (FieldError) error;
            String name = field.getField(); // 필드명
            String message = field.getDefaultMessage(); // 메시지
            String value = field.getRejectedValue().toString(); // 들어온 값

            Error errorMessage = new Error();
            errorMessage.setField(name);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(value);
            errorList.add(errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseAction(errorList, request));
    }

    // @Validated 로 바인딩 validation 실패가 발생하면 ConstraintViolationException 예외가 발생
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException e, HttpServletRequest request) {

        List<Error> errorList = new ArrayList<>();

        e.getConstraintViolations().forEach(error -> {
            // spliterator는 병렬 기능이 추가된(parallel analogue) Iterator
            // StreamSupport 의 stream() 을 통해 스트림 생성
            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            // iterator convert to List
            List<Path.Node> list = stream.collect(Collectors.toList());

            String field = list.get(list.size() - 1).getName();
            String message = error.getMessage();
            String invalidValue = error.getInvalidValue().toString();

            Error errorMessage = new Error();
            errorMessage.setField(field);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(invalidValue);
            errorList.add(errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseAction(errorList, request));
    }

    // 빈값이면 MissingServletRequestParameterException 발생
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        System.out.println("MethodArgumentNotValidException");
        List<Error> errorList = new ArrayList<>();

        String fieldName = e.getParameterName();
        String fieldType = e.getParameterType();
        String invalidValue = e.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    public ErrorResponse errorResponseAction(List<Error> errorList, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(request.getRequestURI());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("Fail");

        return errorResponse;
    }
}
