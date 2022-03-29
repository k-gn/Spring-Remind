package com.study.springjpa.domain.converter;

import com.study.springjpa.repository.dto.BookStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// 자바 객체와 DB 데이터간의 변환 시 가져오는 즉시 정보를 핸들링 할 수 있는 컨버터 클래스
// 컨버터는 코드의 가독성을 높여준다.
//@Converter(autoApply = true) // autoApply = true : 모든 X타입에 자동 적용 (BookStatus 같은 특정 필드타입에만 적용시키는게 안전)
public class BookStatusConverter implements AttributeConverter<BookStatus, Integer> {

    // 서비스에선 조회작업만 하는데도 db 등록 컨버터가 동작한다. ==> 그냥 무조건 둘다 구현하기
    // 데이터가 유실될 수 있으니 둘다 구현해놓는게 좋다.
    @Override
    public Integer convertToDatabaseColumn(BookStatus attribute) {
        System.out.println("convertToDatabaseColumn");
        return attribute.getCode();
    }

    // 컨버터에선 최대한 오류가 나지 않도록 막는게 좋다.
    @Override
    public BookStatus convertToEntityAttribute(Integer dbData) {
        System.out.println("convertToEntityAttribute");
        return dbData != null ?  new BookStatus(dbData) : null;
    }
}
