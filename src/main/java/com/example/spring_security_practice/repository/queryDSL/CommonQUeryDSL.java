package com.example.spring_security_practice.repository.queryDSL;

import com.example.spring_security_practice.domain.entity.Role;

import java.util.List;

public interface CommonQUeryDSL {
    public List<Role> getRolesForAccount(String roleName);
 }
