package com.example.batchsize.application.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.batchsize.domain.pk_identity_strategy.Company;
import com.example.batchsize.domain.pk_identity_strategy.CompanyRepository;
import com.example.batchsize.domain.pk_identity_strategy.Person;
import com.example.batchsize.domain.pk_identity_strategy.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;

	@Transactional
	public void save(Company company) {
		companyRepository.save(company);
	}

	public List<Company> getAll() {
		return companyRepository.findAll();
	}
}
