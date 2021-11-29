package com.example.spring_security_practice.repository.queryDSL;

import com.example.spring_security_practice.domain.entity.Role;

import java.util.List;

public interface CommonQueryDSL {

    List<Role> getRolesForAccount(String roleName);
    List<Role> getRolesForResources(String roleName);

}
