package com.example.spring_security_practice;

import com.example.spring_security_practice.domain.dto.RoleDto;
import com.example.spring_security_practice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestRunner implements ApplicationRunner {

    @Autowired
    RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
        RoleDto roleDto1 = RoleDto.builder()
                .roleName("ROLE_USER").roleDesc("사용자").build();
        RoleDto roleDto2 = RoleDto.builder()
                .roleName("ROLE_ADMIN").roleDesc("사용자").build();
        roleService.createRole(roleDto1);
        roleService.createRole(roleDto2);
        */

    }
}
