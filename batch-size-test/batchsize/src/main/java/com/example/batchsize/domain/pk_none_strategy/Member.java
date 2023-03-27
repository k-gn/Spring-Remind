package com.example.batchsize.domain.pk_none_strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.batchsize.domain.pk_identity_strategy.Company;
import com.example.batchsize.domain.pk_none_strategy.Team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Member {

	@Id
	@Column(name = "member_id")
	private String id;

	private String name;

	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public static Member of(
		String name,
		String email
	) {
		return Member.builder()
			.id(UUID.randomUUID().toString())
			.name(name)
			.email(email)
			.build();
	}

	public void addTeam(Team team) {
		this.team = team;
	}
}
