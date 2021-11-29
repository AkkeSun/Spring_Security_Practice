package com.example.spring_security_practice.service;

import com.example.spring_security_practice.domain.dto.AccountDto;
import com.example.spring_security_practice.domain.entity.Account;

import java.util.List;

public interface AccountService {
     void createUser(AccountDto dto, boolean isAdmin);
     void updateUser(Long id, AccountDto dto);
     List<Account> findAll();
     Account getUser(Long id);
}
