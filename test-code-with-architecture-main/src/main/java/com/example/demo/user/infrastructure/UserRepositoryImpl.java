package com.example.demo.user.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User getById(long id) {
		return findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
	}

	@Override
	public User save(User user) {
		// user.toEntity() 처럼 해줄 수도 있지만, 도메인은 인프라 레이어 정보를 모르는 게 좋다.
		return userJpaRepository.save(UserEntity.from(user)).toModel();
	}

	@Override
	public Optional<User> findById(long id) {
		return userJpaRepository.findById(id).map(UserEntity::toModel);
	}

	@Override
	public Optional<User> findByIdAndStatus(
		long id,
		UserStatus userStatus
	) {
		return userJpaRepository.findByIdAndStatus(id, userStatus).map(UserEntity::toModel);
	}

	@Override
	public Optional<User> findByEmailAndStatus(
		String email,
		UserStatus userStatus
	) {
		return userJpaRepository.findByEmailAndStatus(email, userStatus).map(UserEntity::toModel);
	}
}
