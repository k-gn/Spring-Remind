package com.example.batchsize.application.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.batchsize.domain.pk_identity_strategy.Company;
import com.example.batchsize.domain.pk_identity_strategy.Person;
import com.example.batchsize.domain.pk_none_strategy.Member;
import com.example.batchsize.domain.pk_none_strategy.Team;

@SpringBootTest
class TeamServiceTest {

	@Autowired
	private TeamService teamService;

	@Test
	@DisplayName("팀을 등록합니다.")
	@Rollback(value = false)
	void save() {
		IntStream.range(0, 10).forEach(i -> {
			teamService.save(Team.of("팀" + i));
		});
	}

	@Test
	@DisplayName("팀 목록을 조회합니다.")
	@Transactional
	void getAll() {
		List<Team> all = teamService.getAll();
		for (Team team : all) {
			System.out.println("company.getName() = " + team.getName());
			System.out.println("company.getPersons().size() = " + team.getMembers().size());

			for (Member member : team.getMembers()) {
				System.out.println("person.getName() = " + member.getName());
			}
		}
	}
}