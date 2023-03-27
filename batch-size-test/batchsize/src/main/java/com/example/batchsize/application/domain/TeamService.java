package com.example.batchsize.application.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.batchsize.domain.pk_identity_strategy.Company;
import com.example.batchsize.domain.pk_none_strategy.MemberRepository;
import com.example.batchsize.domain.pk_none_strategy.Team;
import com.example.batchsize.domain.pk_none_strategy.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;

	@Transactional
	public void save(Team team) {
		teamRepository.save(team);
	}

	public List<Team> getAll() {
		return teamRepository.findAll();
	}
}
