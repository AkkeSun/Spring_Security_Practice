package com.example.spring_security_practice.aop;

import org.springframework.stereotype.Service;


@Service
public class AopMethodService {

    public String methodSecure(){
        return "Method Security Test";
    }
}
