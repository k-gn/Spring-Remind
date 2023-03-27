package com.example.batchsize.application.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.example.batchsize.domain.pk_identity_strategy.Company;
import com.example.batchsize.domain.pk_identity_strategy.Person;

@SpringBootTest
class PersonServiceTest {

	@Autowired
	private PersonService personService;

	@Test
	@DisplayName("사람을 등록합니다.")
	@Rollback(value = false)
	void save() {
		IntStream.range(0, 5).forEach(i -> {
			personService.save(Person.of("이름" + i, "이메일" + i), Company.fixture());
		});
	}
}