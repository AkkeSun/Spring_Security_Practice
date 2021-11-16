package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.domain.Account;
import com.example.spring_security_practice.domain.AccountDto;
import com.example.spring_security_practice.repository.AccountRepository;
import com.example.spring_security_practice.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public void createUser(AccountDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = modelMapper.map(dto, Account.class);
        repository.save(account);
    }
}
