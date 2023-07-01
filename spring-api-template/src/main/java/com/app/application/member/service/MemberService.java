package com.app.application.member.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.domain.member.Member;
import com.app.infrastructure.member.repository.MemberJpaRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberJpaRepository memberJpaRepository;

	public Member registerMember(Member member) {
		validateDuplicateMember(member);
		return memberJpaRepository.save(member);
	}

	private void validateDuplicateMember(Member member) {
		Optional<Member> optionalMember = memberJpaRepository.findByEmail(member.getEmail());
		if (optionalMember.isPresent()) {
			throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
		}
	}

	@Transactional(readOnly = true)
	public Optional<Member> findMemberByEmail(String email) {
		return memberJpaRepository.findByEmail(email);
	}

	@Transactional(readOnly = true)
	public Member findMemberByRefreshToken(String refreshToken) {
		Member member = memberJpaRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
		LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();
		if (tokenExpirationTime.isBefore(LocalDateTime.now())) {
			throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		}
		return member;
	}

	public Member findMemberByMemberId(Long memberId) {
		return memberJpaRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));
	}
}
