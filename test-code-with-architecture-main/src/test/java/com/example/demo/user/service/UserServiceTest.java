package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;

@SpringBootTest
@SqlGroup({
	@Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
// @Transactional
class UserServiceTest {

	@Autowired
	private UserService userService;

	@MockBean
	private JavaMailSender javaMailSender;

	@Test
	void getByEmail_은_ACITVE_상태인_유저를_찾아올_수_있다() {
		// given
		String email = "rlarbska97@gmail.com";

		// when
		UserEntity result = userService.getByEmail(email);

		// then
		assertThat(result.getId()).isNotNull();
	}

	@Test
	void getByEmail_은_PENDING_상태인_유저는_찾아올_수_없다() {
		// given
		String email = "rlarbska98@gmail.com";

		// when
		// then
		assertThatThrownBy(() -> userService.getByEmail(email))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void getById_은_ACITVE_상태인_유저를_찾아올_수_있다() {
		// given
		// when
		UserEntity result = userService.getById(1);

		// then
		assertThat(result.getId()).isNotNull();
	}

	@Test
	void getById_은_PENDING_상태인_유저는_찾아올_수_없다() {
		// given
		// when
		// then
		assertThatThrownBy(() -> userService.getById(99999))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void userCreateDto_를_이용하여_유저를_생성한다() {
		// given
		UserCreate userCreate = UserCreate.builder()
			.email("rlarbska99@gmail.com")
			.address("Seoul")
			.nickname("Gyul3")
			.build();
		doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

		// when
		UserEntity result = userService.create(userCreate);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
	}

	@Test
	void userUpdateDto_를_이용하여_유저를_수정한다() {
		// given
		UserUpdate userUpdate = UserUpdate.builder()
			.address("Busan")
			.nickname("Gyul3")
			.build();

		// when
		userService.update(1, userUpdate);

		// then
		UserEntity result = userService.getById(1);
		assertThat(result.getId()).isNotNull();
		assertThat(result.getAddress()).isEqualTo("Busan");
		assertThat(result.getNickname()).isEqualTo("Gyul3");
	}

	@Test
	void user_를_로그인_시키면_마지막_로그인_시간이_변경된다() {
		// given
		// when
		userService.login(1);

		// then
		UserEntity result = userService.getById(1);
		assertThat(result.getLastLoginAt()).isGreaterThan(0);
	}

	@Test
	void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
		// given
		// when
		userService.verifyEmail(2, "aaaaaaa-aaaa-aaaa-aaaaaaaaaaaaa");

		// then
		UserEntity result = userService.getById(2);
		assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
		// given
		// when
		// then
		assertThatThrownBy(() -> userService.verifyEmail(2, "aaaaaaa-aaaa-aaaa-aaaaaaaaaaaac"))
			.isInstanceOf(CertificationCodeNotMatchedException.class);
	}
}