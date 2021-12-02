package com.example.spring_security_practice.service;

import com.example.spring_security_practice.domain.dto.AccountDto;
import com.example.spring_security_practice.domain.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * UserDetailsService
 * DB에 저장된 유저 정보로 시큐리티 인가심사를 하기위한 인터페이스
 */
public interface AccountService extends UserDetailsService {
     void createUser(AccountDto dto, boolean isAdmin);
     void updateUser(Long id, AccountDto dto);
     List<Account> findAll();
     Account getUser(Long id);
}
