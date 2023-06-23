package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;

@SpringBootTest
// @Transactional
@SqlGroup({
	@Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

	@Autowired
	private UserService userService;

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
}