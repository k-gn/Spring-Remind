package com.example.demo.user.service.port;

import java.util.Optional;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;

public interface UserRepository {

	Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);
	Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);
	UserEntity save(UserEntity userEntity);
	Optional<UserEntity> findById(long id);
}
