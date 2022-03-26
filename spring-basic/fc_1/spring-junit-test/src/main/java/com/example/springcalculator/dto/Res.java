package com.example.springcalculator.dto;

import lombok.Data;

@Data
public class Res {

    private int result;

    private Body response;

    @Data
    public static class Body {
        private String resultCode = "OK";
    }
}
