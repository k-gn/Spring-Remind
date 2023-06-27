package com.example.demo.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시물(posts)")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Builder
public class PostController {

	private final PostService postService;

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getById(@PathVariable long id) {
		return ResponseEntity
			.ok()
			.body(PostResponse.from(postService.getById(id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostResponse> update(
		@PathVariable long id,
		@RequestBody PostUpdate postUpdate
	) {
		return ResponseEntity
			.ok()
			.body(PostResponse.from(postService.update(id, postUpdate)));
	}
}