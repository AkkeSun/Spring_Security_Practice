package com.example.spring_security_practice.security.common.service;


import com.example.spring_security_practice.domain.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;

import java.util.Collection;

public class AccountContext extends User {

    private final Account account;

    public AccountContext( Account account, Collection<? extends GrantedAuthority> authorities ) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }

    public Account getAccount(){
        return this.account;
    }
}
