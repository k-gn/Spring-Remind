package com.example.validation.dto;

import com.example.validation.annotation.YearMonth;
import com.example.validation.dto.Car;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Spring Validation
// valid는 꼭 들어가야 하는 값들에 설정해주자.
public class User {

    @NotBlank
    private String name;

    @Max(90)
    private int age;

    @Email // 이메일 양식 검증
    private String email;

    // 정규표현식을 사용
    // message 로 에러 발생 시 보여줄 메시지를 지정
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")
    private String phoneNumber;

    //@Size(min = 6, max = 6)
    @YearMonth
    private String reqYearMonth; // yyyyMM

    // 클래스 내에서도 다른 특정 클래스의 변수를 검사하고 싶으면 @Valid를 꼭 붙여줘야 한다.
    @Valid
    private List<Car> cars;

    // 메소드를 통한 날짜 검증
    // @AssertTrue / False -> return이 true or false면 정상 (단. 재사용이 불가)
//    @AssertTrue(message = "yyyyMM 형식에 맞지 않습니다.")
//    public boolean isReqYearMonthValidation() { // boolean method 는 메소드명 앞에 is 키워드를 붙여 작성해야한다.
//      try {
//        // LocalDate은 기본적으로 dd까지 들어가야 한다.
//        LocalDate localDate = LocalDate.parse(getReqYearMonth() + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
//      }catch (Exception e) {
//          return false;
//       }
//           return true;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReqYearMonth() {
        return reqYearMonth;
    }

    public void setReqYearMonth(String reqYearMonth) {
        this.reqYearMonth = reqYearMonth;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", reqYearMonth='" + reqYearMonth + '\'' +
                ", cars=" + cars +
                '}';
    }
}
