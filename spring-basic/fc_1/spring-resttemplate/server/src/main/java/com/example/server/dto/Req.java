package com.example.server.dto;

import lombok.Data;

@Data
public class Req<T> {

    private Header header;
    private T body;

    @Data
    public static class Header {
        private String responseCode;
    }
}
