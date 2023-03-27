package com.example.batchsize.domain.pk_identity_strategy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.batchsize.domain.pk_identity_strategy.Person;
import com.example.batchsize.domain.pk_none_strategy.Member;
import com.example.batchsize.domain.pk_none_strategy.Team;

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
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_id")
	private Long id;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "company")
	private final List<Person> persons = new ArrayList<>();

	public void addPerson(Person person) {
		this.persons.add(person);
		person.addCompany(this);
	}

	public static Company of(String name) {
		return Company.builder()
			.name(name)
			.build();
	}

	public static Company fixture() {
		return Company.builder()
			.id(3L)
			.build();
	}
}
