package com.example.batchsize.application.domain;

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

@SpringBootTest
class CompanyServiceTest {

	@Autowired
	private CompanyService companyService;

	@Test
	@DisplayName("회사를 등록합니다.")
	void save() {
		IntStream.range(0, 10).forEach(i -> {
			companyService.save(Company.of("회사" + i));
		});
	}

	@Test
	@DisplayName("회사 목록을 조회합니다.")
	@Transactional
	void getAll() {
		List<Company> all = companyService.getAll();
		for (Company company : all) {
			System.out.println("company.getName() = " + company.getName());
			System.out.println("company.getPersons().size() = " + company.getPersons().size());

			for (Person person : company.getPersons()) {
				System.out.println("person.getName() = " + person.getName());
			}
		}
	}
}