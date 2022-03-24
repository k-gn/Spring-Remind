package com.fc.springweb.controller;

import com.fc.springweb.dto.PutRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PutApiController {

    // Put (수정, 멱등함)
    @PutMapping("/put/{userId}")
    public PutRequestDto put(@RequestBody PutRequestDto requestDto, @PathVariable Long userId) {
        System.out.println(requestDto);
        System.out.println(userId);
        // DTO 에서 지정한 JsonProperty 또는 JsonNaming 에 따라서 Json으로 바껴서 Response가 된다.
        return requestDto;
    }

}
