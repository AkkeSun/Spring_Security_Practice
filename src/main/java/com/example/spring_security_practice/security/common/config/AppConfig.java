package com.example.spring_security_practice.security.common.config;

import com.example.spring_security_practice.repository.ResourcesRepository;
import com.example.spring_security_practice.security.common.service.SecurityResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 공용으로 사용하는 빈 생성
 */
@Configuration
public class AppConfig {

    @Bean
    public SecurityResourceService resourceService(ResourcesRepository resourcesRepository){
        SecurityResourceService securityResourceService = new SecurityResourceService(resourcesRepository);
        return securityResourceService;
    }

}
