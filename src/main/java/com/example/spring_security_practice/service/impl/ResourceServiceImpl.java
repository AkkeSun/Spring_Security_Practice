package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.domain.dto.ResourcesDto;
import com.example.spring_security_practice.domain.entity.Resources;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.ResourcesRepository;
import com.example.spring_security_practice.repository.RoleRepository;
import com.example.spring_security_practice.service.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void createResource(ResourcesDto dto) {
        Set<Role> roles = new HashSet<>();
        List<Role> roleList = roleRepository.getRolesForAccount(dto.getRoleName());
        roleList.forEach( role -> roles.add(role) );

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setRoleSet(roles);

        resourcesRepository.save(resources);
    }

    @Transactional
    public List<Resources> getResources() {
        return resourcesRepository.findAll();
    }

    @Transactional
    public Resources getResource(Long id) {
        return resourcesRepository.findById(id).get();
    }

    @Transactional
    public void deleteResource(Long id) {
        resourcesRepository.deleteById(id);
    }

    @Transactional
    public void updateResources(Long id, ResourcesDto dto) {
        Resources check = resourcesRepository.findById(id).get();

        if(check == null)
            throw new NullPointerException("Resources Not Found");

        Set<Role> roles = new HashSet<>();
        List<Role> roleList = roleRepository.getRolesForAccount(dto.getRoleName());
        roleList.forEach( role -> roles.add(role) );

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setRoleSet(roles);
        resources.setId(id);

        resourcesRepository.save(resources);
    }
}
