package com.example.spring_security_practice.security.common.service;

import com.example.spring_security_practice.domain.Account;
import com.example.spring_security_practice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDetailsService
 * DB에 저장된 유저 정보를 가져오는 클래스
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    AccountRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Account account = repository.findByUsername(username);
        if(account == null)
            throw new UsernameNotFoundException("Invalid Username or Password");

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));
        AccountContext accountContext = new AccountContext(account, roles);

        return accountContext;
    }
}