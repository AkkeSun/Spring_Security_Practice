package com.example.spring_security_practice.security.common.service;

import com.example.spring_security_practice.domain.entity.Resources;
import com.example.spring_security_practice.repository.ResourcesRepository;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * DB에서 자원정브롤 가져와 LinkedHashMap으로 저장
 */

public class SecurityResourceService {

    private ResourcesRepository resourcesRepository;

    public SecurityResourceService (ResourcesRepository resourcesRepository){
        this.resourcesRepository=resourcesRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourceList = resourcesRepository.findAllUrlResources();

        resourceList.forEach(resource -> {
            List<ConfigAttribute> roleList = new ArrayList<>();
            resource.getRoleSet().forEach(role -> {
                roleList.add(new SecurityConfig(role.getRoleName()));
                result.put(new AntPathRequestMatcher(resource.getResourceName()), roleList);
            });
        });

        return result;
    }
}
