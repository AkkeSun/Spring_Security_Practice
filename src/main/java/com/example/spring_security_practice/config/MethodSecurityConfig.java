package com.example.spring_security_practice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * [ Method 기반 방식의 인가처리를 위한 Config ]
 *
 * // 필수 어노테이션 (Method 기반 인가처리 활성화)
 * @EnableGlobalMethodSecurity
 *
 * // 어노테이션 기반 Method 인가처리를 하는 경우는 옵션을 추가
 * @EnableGlobalMethodSecurity (prePostEnabled = true, securedEnabled = true)
 *
 * // 필수 상속 class
 * GlobalMethodSecurityConfiguration
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource;
    }

}


