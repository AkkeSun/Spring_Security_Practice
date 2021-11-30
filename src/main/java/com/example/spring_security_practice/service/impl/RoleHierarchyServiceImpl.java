package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.RoleRepository;
import com.example.spring_security_practice.service.RoleHierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * [RoleHierarchy DB Format]
     * ROLE_ADMIN > ROLE_MANAGER
     * ROLE_MANAGER > ROLE_USER
     */
    @Transactional
    public String findAllHierarchy() {

        List<Role> roles = roleRepository.findAll();
        StringBuffer concatedRoles = new StringBuffer();

        for(int i=0; i<roles.size(); i++){
            if(i+1 < roles.size() && i>0){
                concatedRoles.append(roles.get(i).getRoleName());
                concatedRoles.append(" > ");
                concatedRoles.append(roles.get(i+1).getRoleName());
                concatedRoles.append("\n");
            }

        }
        return concatedRoles.toString();
    }
}
