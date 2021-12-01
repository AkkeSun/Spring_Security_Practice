package com.example.spring_security_practice.security.AjaxLogin.provier;

import com.example.spring_security_practice.security.AjaxLogin.token.AjaxAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

/**
 * AuthenticationProvider
 * Ajax 로그인 검증을 담당하는 클래스
 */
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 유저가 입력한 로그인 데이터가 담긴 authentication 객채
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // DB에 저장된 유저 정보가 담겨있는 객채
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // DB데이터와 로그인 데이터가 일치한지 검증
        if(!(passwordEncoder.matches(password, userDetails.getPassword())))
            throw new BadCredentialsException("Invalid Username or Password");

        // 인승 성공하면 토큰 리턴
        return new AjaxAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    @Override
    // Authentication과 해당 토큰이 같을 때 구동
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AjaxAuthenticationToken.class);
    }
}