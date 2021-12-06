package com.example.spring_security_practice.config;

import com.example.spring_security_practice.factory.MethodResourcesMapFactoryBean;
import com.example.spring_security_practice.factory.UrlResourcesMapFactoryBean;
import com.example.spring_security_practice.metaDataSource.UrlMetadataSource;
import com.example.spring_security_practice.service.AccessIpService;
import com.example.spring_security_practice.service.security.SecurityResourceService;
import com.example.spring_security_practice.voter.IpAddressVoter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * 공용으로 사용하는 빈 생성
 */
@Configuration
public class WebConfig {

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private AccessIpService accessIpService;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    // ============================ Role 계층권한 적용 ================================
    // RoleHierarchy 에 들어가는 권한 정보는 SecurityInitializer 에서 입력된다
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return  new RoleHierarchyImpl();
    }
    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }



    // =========================== factory bean ===============================
    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(){
        return new UrlResourcesMapFactoryBean(securityResourceService);
    }

    @Bean
    public MethodResourcesMapFactoryBean methodResourcesMapFactoryBean(){
        return new MethodResourcesMapFactoryBean(securityResourceService);
    }



    // =========================== MetaData Source ===============================
    @Bean
    public UrlMetadataSource urlMetadataSource() throws Exception {
        return new UrlMetadataSource(urlResourcesMapFactoryBean().getObject());
    }
    
    @Bean // 스프링 기본제공
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource(){
        return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
    }





    // =============================인가처리 필터 접근결정 매니저 ==============================
    @Bean
    public AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();

        accessDecisionVoters.add(new IpAddressVoter(accessIpService));   // Ip 심사 (이게 가장 먼저)
        accessDecisionVoters.add(roleVoter());                           // Role 계층권한 적용

        return new AffirmativeBased(accessDecisionVoters);
    }

}
