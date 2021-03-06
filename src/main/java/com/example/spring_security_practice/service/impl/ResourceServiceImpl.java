package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.domain.dto.ResourcesDto;
import com.example.spring_security_practice.domain.entity.Resources;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.ResourcesRepository;
import com.example.spring_security_practice.repository.RoleRepository;
import com.example.spring_security_practice.metaDataSource.UrlMetadataSource;
import com.example.spring_security_practice.service.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UrlMetadataSource urlFilterInvocationSecurityMetadataSource;

    @Transactional
    public void createResource(ResourcesDto dto) {

        // role setting
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleName(dto.getRoleName()));

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setRoleSet(roles);

        resourcesRepository.save(resources);
        // 실시간 업데이트
        urlFilterInvocationSecurityMetadataSource.reload();
    }

    @Transactional
    public List<Resources> getResources() {
        return resourcesRepository.findAllResources();
    }

    @Transactional
    public Resources getResource(Long id) {
        return resourcesRepository.findById(id).get();
    }

    @Transactional
    public void updateResources(Long id, ResourcesDto dto) {
        Resources check = resourcesRepository.findById(id).get();

        if(check == null)
            throw new NullPointerException("Resources Not Found");

        // role setting
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleName(dto.getRoleName()));

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setRoleSet(roles);
        resources.setId(id);

        resourcesRepository.save(resources);
        // 실시간 업데이트
        urlFilterInvocationSecurityMetadataSource.reload();
    }

}
