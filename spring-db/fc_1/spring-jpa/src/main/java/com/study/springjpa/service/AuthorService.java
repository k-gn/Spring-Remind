package com.study.springjpa.service;

import com.study.springjpa.domain.Author;
import com.study.springjpa.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void put() {
        Author author = new Author();
        author.setName("martin");
        authorRepository.save(author);
    }
}
