package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;

/*
	- 중형 테스트는 스프링과 h2 등을 올리는 시간 소모로 테스트 속도가 느리다
	- 소형 테스트로 변환하여 빠른 속도로 테스트가 가능하다.
 */
class UserServiceTest {

	private UserServiceImpl userService;

	// fixture
	@BeforeEach
	void init() {
		FakeMailSender fakeMailSender = new FakeMailSender();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.userService = UserServiceImpl.builder()
			.uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
			.clockHolder(new TestClockHolder(1678530673958L))
			.userRepository(fakeUserRepository)
			.certificationService(new CertificationService(fakeMailSender))
			.build();
		fakeUserRepository.save(User.builder()
			.id(1L)
			.email("kok202@naver.com")
			.nickname("kok202")
			.address("Seoul")
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.status(UserStatus.ACTIVE)
			.lastLoginAt(0L)
			.build());
		fakeUserRepository.save(User.builder()
			.id(2L)
			.email("kok303@naver.com")
			.nickname("kok303")
			.address("Seoul")
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
			.status(UserStatus.PENDING)
			.lastLoginAt(0L)
			.build());
	}

	@Test
	void getByEmail_은_ACITVE_상태인_유저를_찾아올_수_있다() {
		// given
		String email = "kok202@naver.com";

		// when
		User user = userService.getByEmail(email);

		// then
		assertThat(user.getId()).isNotNull();
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
		User user = userService.getById(1);

		// then
		assertThat(user.getId()).isNotNull();
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
	void userCreate를_이용하여_유저를_생성한다() {
		// given
		UserCreate userCreate = UserCreate.builder()
			.email("rlarbska99@gmail.com")
			.address("Seoul")
			.nickname("Gyul3")
			.build();

		// when
		User user = userService.create(userCreate);

		/*
			테스트 하나에 assert 는 되도록 적은게 좋긴 하지만,
			필수로 지켜야할 사항은 아니다.
			-> 어떤 테스트인지, 뭐가 중요한지에 대한 표현력이 핵심이다.
		 */
		// then
		assertThat(user.getId()).isNotNull();
		assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
	}

	@Test
	void userUpdate를_이용하여_유저를_수정한다() {
		// given
		UserUpdate userUpdate = UserUpdate.builder()
			.address("Busan")
			.nickname("Gyul3")
			.build();

		// when
		userService.update(1, userUpdate);

		// then
		User user = userService.getById(1);
		assertThat(user.getId()).isNotNull();
		assertThat(user.getAddress()).isEqualTo("Busan");
		assertThat(user.getNickname()).isEqualTo("Gyul3");
	}

	@Test
	void user_를_로그인_시키면_마지막_로그인_시간이_변경된다() {
		// given
		// when
		userService.login(1);

		// then
		User user = userService.getById(1);
		assertThat(user.getLastLoginAt()).isGreaterThan(0);
		assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
	}

	@Test
	void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
		// given
		// when
		userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

		// then
		User user = userService.getById(2);
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
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
/*
	# 테스트 tip

	1. ParameterizedTest
	2. assertAll
	3. 하나의 테스트는 하나의 개념
	4. Thread.sleep
		- 비동기 처리 테스트 시 Thread.sleep 은 개별 데스크탑에 따라 결과가 달라질 수 있다.
			- Thread join (범용적인 해결책은 아니다.)
			- Awaitility 같은 라이브러리를 활용
	5. FIRST
		- Fast : 빨라야한다.
		- Independent : 독립적이다.
		- Repeatable : 반복해도 같은 결과가 나온다.
		- Self-validating : 성공/실패를 알아야한다.
		- Timely : 적시에 작성해야한다.
 */