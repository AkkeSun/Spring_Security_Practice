package com.example.spring_security_practice.security.common.service;

import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
            throw new UsernameNotFoundException("No user found with username: " + username);

        return new User(account.getUsername(), account.getPassword(), authorities(account.getUserRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority( r.getRoleName()))
                .collect(Collectors.toSet());
    }
}