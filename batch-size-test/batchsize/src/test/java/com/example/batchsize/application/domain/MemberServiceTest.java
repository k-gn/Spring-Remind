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
import com.example.batchsize.domain.pk_none_strategy.Member;
import com.example.batchsize.domain.pk_none_strategy.Team;

@SpringBootTest
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("사람을 등록합니다.")
	@Rollback(value = false)
	void save() {
		IntStream.range(0, 5).forEach(i -> {
			memberService.save(Member.of("이름" + i, "이메일" + i), Team.fixture());
		});
	}
}