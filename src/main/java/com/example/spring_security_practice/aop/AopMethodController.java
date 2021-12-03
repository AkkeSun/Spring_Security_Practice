package com.example.spring_security_practice.aop;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AopMethodController {

    @Autowired
    private AopMethodService aopMethodService;

    @GetMapping("/preAuth")
    public String preAuth(){
        return aopMethodService.methodSecure();
    }

    @GetMapping("/securedAuth")
    public String securedAuth(){
        return aopMethodService.methodSecure();
    }
}
