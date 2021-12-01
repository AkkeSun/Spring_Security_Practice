package com.example.spring_security_practice.security.AjaxLogin.filter;

import com.example.spring_security_practice.domain.dto.AccountDto;
import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.repository.AccountRepository;
import com.example.spring_security_practice.security.AjaxLogin.token.AjaxAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
 * ajax 로그인 요청처리 filter 
 * UsernamePasswordAuthenticationFilter 대신 사용
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private AccountRepository accountRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    // 요청받을 login url
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
        AjaxAuthenticationToken token = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

        // details 설정
        setDetails(request, token);

        // AuthenticationManager 에게 전달
        return getAuthenticationManager().authenticate(token);
    }

    // ajax 요청은 header에 X-Requested-with : XMLHttpRequest 을 실어보낸다
    private boolean isAjax(HttpServletRequest request) {
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-with")))
            return true;
        return false;
    }

    protected void setDetails(HttpServletRequest request, AjaxAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}