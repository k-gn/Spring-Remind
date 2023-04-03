package com.example.rest_docs.member;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApi {

	private final MemberRepository memberRepository;

	/**
	 * 1. Member 단일 조회
	 * 2. Member 생성
	 * 3. Member 수정
	 * 4. Member 페이징 조회
	 */

	@GetMapping("/{id}")
	public MemberResponse getMember(@PathVariable Long id) {
		Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not Found"));
		return new MemberResponse(member);
	}

	@PostMapping
	public void createMember(@RequestBody @Valid MemberSignUpRequest request) {
		Member member = request.toEntity();
		memberRepository.save(member);
	}

	@PutMapping("/{id}")
	public void modifyMember(
		@PathVariable Long id,
		@RequestBody @Valid MemberModificationRequest request
	) {
		final Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Not Found"));
		member.modify(request.getName());
	}

	@GetMapping
	public Page<MemberResponse> getMembers(
		@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return memberRepository.findAll(pageable).map(MemberResponse::new);
	}
}
