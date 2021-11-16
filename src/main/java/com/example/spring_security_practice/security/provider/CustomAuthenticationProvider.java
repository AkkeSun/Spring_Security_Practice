package com.example.spring_security_practice.security.provider;

import com.example.spring_security_practice.security.service.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 로그인 데이터가 담긴 authentication 객채
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // DB에 저장된 유저 정보가 담겨있는 객채
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        // DB데이터와 로그인 데이터가 일치한지 검증
        if(passwordEncoder.matches(password, accountContext.getAccount().getPassword()))
            throw new BadCredentialsException("BadCredentialsException");

        // 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
