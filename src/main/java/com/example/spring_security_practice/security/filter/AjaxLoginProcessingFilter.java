package com.example.spring_security_practice.security.filter;

import com.example.spring_security_practice.domain.Account;
import com.example.spring_security_practice.domain.AccountDto;
import com.example.spring_security_practice.security.token.AjaxAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ajax 로그인 요청처리
 * 1. 해당 url로 들어온 로그인 중 ajax 요청이라면 실행
 * 2. AjaxAuthenticationToken 생성 후 AuthenticationManager에게 전달
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    // 해당 url만 요청받아라 (public으로 만들기)
    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        // ajax 요청인지 확인
        if(!isAjax(request))
            throw new IllegalStateException("Authentication is not supported");
        
        // user에게 요청받은 데이터로 dto 객채 생성
        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if(StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword()))
            throw new IllegalArgumentException("Username or Password is empty");

        // 인증 받기 전 username, password 정보만 가진 토큰 생성
        AjaxAuthenticationToken authenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

        // AuthenticationManager에게 전달
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    // ajax 요청은 header에 X-Requested-with : XMLHttpRequest 을 실어보낸다
    private boolean isAjax(HttpServletRequest request) {
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-with")))
            return true;
        return false;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
