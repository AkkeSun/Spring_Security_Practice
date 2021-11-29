package com.example.spring_security_practice.service;

import com.example.spring_security_practice.domain.dto.RoleDto;
import com.example.spring_security_practice.domain.entity.Role;

import java.util.List;


public interface RoleService {

    Role getRole(long id);

    List<Role> getRoles();

    void createRole(RoleDto roledto);

    void updateRole(Long id, RoleDto roledto);

}