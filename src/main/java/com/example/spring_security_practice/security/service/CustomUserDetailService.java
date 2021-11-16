package com.example.spring_security_practice.security.service;

import com.example.spring_security_practice.domain.Account;
import com.example.spring_security_practice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * security login 인증을 담당하는 UserDetailsService 커스터마이징
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Account account = repository.findByUsername(username);
        if(account == null)
            throw new UsernameNotFoundException("UserName Not Found Exception");

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));
        // DB에 저장된 유저 정보가 담겨있는 객채
        AccountContext accountContext = new AccountContext(account, roles);

        return accountContext;
    }
}