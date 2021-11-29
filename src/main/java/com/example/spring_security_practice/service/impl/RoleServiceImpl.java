package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.domain.dto.RoleDto;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.RoleRepository;
import com.example.spring_security_practice.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
}
