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
import com.example.batchsize.domain.pk_identity_strategy.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Team {

	@Id
	@Column(name = "team_id")
	private String id;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "team")
	private final List<Member> members = new ArrayList<>();

	public void addMember(Member member) {
		this.members.add(member);
		member.addTeam(this);
	}

	public static Team of(String name) {
		return Team.builder()
			.id(UUID.randomUUID().toString())
			.name(name)
			.build();
	}

	public static Team fixture() {
		return Team.builder()
			.id("5b92f573-c8f1-44e7-abc1-4503b84c3b0c")
			.build();
	}
}
