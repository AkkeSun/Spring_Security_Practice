package com.example.spring_security_practice.provier;

import com.example.spring_security_practice.AuthenticationDetails.FormWebAuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * AuthenticationProvider
 * 로그인 검증을 담당하는 클래스
 */
@Component
public class FormAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 로그인 데이터가 담긴 authentication 객채
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // DB에 저장된 유저 정보가 담겨있는 객채
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // DB데이터와 로그인 데이터가 일치한지 검증
        if(!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Invalid Username or Password");

        // formWebAuthenticationDetails로 가져온 파라미터(secretKey)가 일치하는지 검증
        FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = formWebAuthenticationDetails.getSecretKey();
        if(secretKey == null || !"secret".equals(secretKey))
            throw new InsufficientAuthenticationException("Invalid SecretKey");

        // 인승 성공하면 토큰 생성 (UserDetail, password, 권한정보)
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    // Authentication과 해당 토큰이 같을 때 구동
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
