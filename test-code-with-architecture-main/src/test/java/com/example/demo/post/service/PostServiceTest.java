package com.example.demo.post.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class PostServiceTest {

	private PostServiceImpl postService;

	@BeforeEach
	void init() {
		FakePostRepository fakePostRepository = new FakePostRepository();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.postService = PostServiceImpl.builder()
			.postRepository(fakePostRepository)
			// 테스트 데이터 짜는게 귀찮다? -> 테스트가 보내는 신호 -> 의존성을 좀 줄여봐라~
			.userRepository(fakeUserRepository)
			.clockHolder(new TestClockHolder(1679530673958L))
			.build();
		User user1 = User.builder()
			.id(1L)
			.email("kok202@naver.com")
			.nickname("kok202")
			.address("Seoul")
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.status(UserStatus.ACTIVE)
			.lastLoginAt(0L)
			.build();
		User user2 = User.builder()
			.id(2L)
			.email("kok303@naver.com")
			.nickname("kok303")
			.address("Seoul")
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
			.status(UserStatus.PENDING)
			.lastLoginAt(0L)
			.build();
		fakeUserRepository.save(user1);
		fakeUserRepository.save(user2);
		fakePostRepository.save(Post.builder()
			.id(1L)
			.content("helloworld")
			.createdAt(1678530673958L)
			.modifiedAt(0L)
			.writer(user1)
			.build());
	}

	@Test
	void getById는_존재하는_게시물을_내려준다() {
		// given
		// when
		Post result = postService.getById(1);

		// then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("kok202@naver.com");
		assertThat(result.getCreatedAt()).isEqualTo(1678530673958L);
	}

	@Test
	void postCreate를_이용하여_게시물을_생성할_수_있다() {
		// given
		PostCreate postCreate = PostCreate.builder()
			.writerId(1)
			.content("foobar")
			.build();

		// when
		Post post = postService.create(postCreate);

		// then
		assertThat(post.getId()).isNotNull();
		assertThat(post.getContent()).isEqualTo("foobar");
		assertThat(post.getCreatedAt()).isEqualTo(1679530673958L);
	}

	@Test
	void postUpdate를_이용하여_게시물을_수정할_수_있다() {
		// given
		PostUpdate postUpdate = PostUpdate.builder()
			.content("hello world :)")
			.build();

		// when
		postService.update(1, postUpdate);

		// then
		Post post = postService.getById(1);
		assertThat(post.getContent()).isEqualTo("hello world :)");
		assertThat(post.getModifiedAt()).isEqualTo(1679530673958L);
	}
}