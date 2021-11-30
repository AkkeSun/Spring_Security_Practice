package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.domain.dto.RoleDto;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.RoleRepository;
import com.example.spring_security_practice.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public Role getRole(long id) {
        return roleRepository.findById(id).get();
    }

    @Transactional
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public void createRole(RoleDto roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        roleRepository.save(role);
    }

    @Transactional
    public void updateRole(Long id, RoleDto roledto) {
        Role check = roleRepository.findById(id).get();

        if(check == null)
            throw new NullPointerException("Role Not Found");
        Role role = modelMapper.map(roledto, Role.class);
        role.setId(id);
        roleRepository.save(role);
    }



    /**
     * [Role 계층 권한 적용을 위한 format]
     * ROLE_ADMIN > ROLE_MANAGER
     * ROLE_MANAGER > ROLE_USER
     */
    @Transactional
    public String findAllHierarchy() {

        List<Role> roles = roleRepository.findAll();
        StringBuffer concatedRoles = new StringBuffer();

        if(roles.size() <= 1)
            return "";

        for(int i=0; i<roles.size(); i++){
            if(i+1 < roles.size()){
                concatedRoles.append(roles.get(i).getRoleName());
                concatedRoles.append(" > ");
                concatedRoles.append(roles.get(i+1).getRoleName());
                concatedRoles.append("\n");
            }
        }
        return concatedRoles.toString();
    }
}
