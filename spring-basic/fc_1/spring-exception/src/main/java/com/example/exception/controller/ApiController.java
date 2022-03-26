package com.example.exception.controller;

import com.example.exception.dto.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController("ApiController3")
@RequestMapping("/apie")
@Validated // 해당 클래스에서 검증을 가능하게 해준다.
public class ApiController {

    @GetMapping("")
    public User get(
            @Size(min = 2)
            @RequestParam(required = false) String name,

            @NotNull
            @Min(1)
            @RequestParam(required = false) Integer age) {
//            @RequestParam(required = false) Integer age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);

        return user;
    }

    @PostMapping("")
    public User post(@Valid @RequestBody User user) {
        System.out.println(user);
        return user;
    }

    // 특정 예외설정은 글로벌 설정보다 해당 컨트롤러 내에 에러처리의 우선순위가 더 높다.
    // 해당 컨트롤러내에서 관여하게 된다.
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e) {
//        System.out.println("api controller");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    }
}
