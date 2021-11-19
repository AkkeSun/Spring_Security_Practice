package com.example.spring_security_practice.security.FormLogin.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AccessDeniedHandler
 * 인가 예외 처리 핸들러
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        String deniedURL = errorPage + "?exception=" + e.getMessage();
        httpServletResponse.sendRedirect(deniedURL);
    }

    public void setErrorPage(String errorPage){
        this.errorPage = errorPage;
    }
}
