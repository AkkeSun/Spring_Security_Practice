package com.example.spring_security_practice.handler.form;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AccessDeniedHandler
 * 인가 예외 처리 핸들러
 */
@Component
public class FormAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage = "/denied";

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        String deniedURL = errorPage + "?exception=" + e.getMessage();
        httpServletResponse.sendRedirect(deniedURL);
    }
}
