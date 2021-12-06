package com.example.spring_security_practice.aop;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class AopMethodService {

    public String methodSecure(){
        return "Method Security Test";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String preAuthorize(){
        return "preAuthorize";
    }

    @PostAuthorize("hasRole('ROLE_ADMIN')")
    public String postAuthorize(){
        return "postAuthorize";
    }

    @Secured("ROLE_ADMIN")
    public String secured(){
        return "secured";
    }
}
