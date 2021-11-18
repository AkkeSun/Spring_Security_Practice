package com.example.spring_security_practice.security.common;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * WebAuthenticationDetails
 * 인증처리 과정에서 사용자가 전달하는 추가적인 파라미터를 저장하는 클래스
 */
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    // 추가로 받아들일 파라미터 선언
    private String secretKey;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secretKey");
    }

    public String getSecretKey(){
        return secretKey;
    }
}
