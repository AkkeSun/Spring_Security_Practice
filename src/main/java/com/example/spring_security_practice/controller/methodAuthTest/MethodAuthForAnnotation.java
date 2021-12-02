package com.example.spring_security_practice.controller.methodAuthTest;

import com.example.spring_security_practice.service.methodAuth.MethodAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SecurityConfig에
 * @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
 * 붙이기
 */

@RestController
public class MethodAuthForAnnotation {

    @Autowired
    private MethodAuthService service;

    @GetMapping("/method1")
    public String preAuthorize(){
        String str = service.preAuthorize();
        return str;
    }

    @GetMapping("/method2")
    public String postAuthorize(){
        String str = service.postAuthorize();
        return str;
    }

    @GetMapping("/method3")
    public String secured(){
        String str = service.secured();
        return str;
    }
}
