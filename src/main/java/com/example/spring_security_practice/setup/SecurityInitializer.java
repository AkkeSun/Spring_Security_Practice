package com.example.spring_security_practice.setup;

import com.example.spring_security_practice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;


@Component
public class SecurityInitializer implements ApplicationRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Role roleHierarchy 정보 등록
        String allHierarchy = roleService.findAllHierarchy();
        roleHierarchy.setHierarchy(allHierarchy);
    }
}
