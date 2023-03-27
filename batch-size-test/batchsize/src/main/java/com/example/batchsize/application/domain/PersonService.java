package com.example.batchsize.application.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.batchsize.domain.pk_identity_strategy.Company;
import com.example.batchsize.domain.pk_identity_strategy.Person;
import com.example.batchsize.domain.pk_identity_strategy.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

	private final PersonRepository personRepository;

	@Transactional
	public void save(
		Person person,
		Company company
	) {
		person.addCompany(company);
		personRepository.save(person);
	}
}
