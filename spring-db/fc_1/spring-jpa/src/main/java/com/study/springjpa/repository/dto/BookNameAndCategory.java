package com.study.springjpa.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Martin
 * @since 2021/06/09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookNameAndCategory { // 인터페이스로도 받을 수 있다.
    private String name;
    private String category;
    
//    String getName();
//    String getCategory();
}