package com.example.spring_security_practice.common;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
