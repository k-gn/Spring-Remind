package com.example.demo.post.service;

import org.springframework.stereotype.Service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.port.UserRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@Builder
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	// private final PostJpaRepository postJpaRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final ClockHolder clockHolder;

	public Post getById(long id) {
		return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
	}

	public Post create(PostCreate postCreate) {
		User user = userRepository.getById(postCreate.getWriterId());
		Post post = Post.from(user, postCreate, clockHolder);
		return postRepository.save(post);
	}

	public Post update(
		long id,
		PostUpdate postUpdate
	) {
		Post post = getById(id);
		post = post.update(postUpdate, clockHolder);
		return postRepository.save(post);
	}
}