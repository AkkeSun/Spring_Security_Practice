package com.example.spring_security_practice.service.security;

import com.example.spring_security_practice.domain.entity.Resources;
import com.example.spring_security_practice.repository.ResourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SecurityResourceService {

    @Autowired
    private ResourcesRepository resourcesRepository;

    // ========================= URL Resource 최종 Return ==========================
    // LinkedHashMap ( path : roleList )
    @Transactional
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getUrlResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourceList = resourcesRepository.findAllUrlResources();

        resourceList.forEach(resource -> {
            List<ConfigAttribute> roleList = new ArrayList<>();
            resource.getRoleSet().forEach(role -> {
                roleList.add(new SecurityConfig(role.getRoleName()));
            });
            result.put(new AntPathRequestMatcher(resource.getResourceName()), roleList);
        });

        return result;
    }


    // ========================= Method Resource 최종 Return ==========================
    // LinkedHashMap ( path : roleList )
    @Transactional
    public LinkedHashMap<String, List<ConfigAttribute>> getMethodResourceList() {

        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourceList = resourcesRepository.findAllMethodResources();

        resourceList.forEach(resource -> {
            List<ConfigAttribute> roleList = new ArrayList<>();
            resource.getRoleSet().forEach(role -> {
                roleList.add(new SecurityConfig(role.getRoleName()));
            });
            result.put(resource.getResourceName(), roleList);
        });

        return result;
    }
}
