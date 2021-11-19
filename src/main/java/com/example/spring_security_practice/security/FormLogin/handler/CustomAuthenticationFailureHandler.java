package com.example.spring_security_practice.security.FormLogin.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * SimpleUrlAuthenticationFailureHandler
 * login 실패시 실행되는 handler
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        setDefaultFailureUrl("/login?error=true&exception=" + e.getMessage());
        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e); // 위임
    }
}
