package com.example.demo.user.service;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.port.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	// private final UserJpaRepository userJpaRepository;
	private final UserRepository userRepository;
	// private final JavaMailSender mailSender;
	private final CertificationService certificationService;
	private final UuidHolder uuidHolder;
	private final ClockHolder clockHolder;

	public Optional<User> findById(long id) {
		return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE);
	}

	public User getByEmail(String email) {
		return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
			.orElseThrow(() -> new ResourceNotFoundException("Users", email));
	}

	public User getById(long id) {
		return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
			.orElseThrow(() -> new ResourceNotFoundException("Users", id));
	}

	@Transactional
	public User create(UserCreate userCreate) {
		User user = User.from(userCreate, uuidHolder);
		user = userRepository.save(user);
		certificationService.certificate(userCreate.getEmail(), user.getId(), user.getCertificationCode());
		return user;
	}

	@Transactional
	public User update(
		long id,
		UserUpdate userUpdate
	) {
		User user = getById(id);
		user = user.update(userUpdate);
		return userRepository.save(user);
	}

	@Transactional
	public void login(long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
		user = user.login(clockHolder);
		userRepository.save(user);
	}

	@Transactional
	public void verifyEmail(
		long id,
		String certificationCode
	) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
		user = user.certificate(certificationCode);
		userRepository.save(user);
	}
}