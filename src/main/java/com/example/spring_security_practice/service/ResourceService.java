package com.example.spring_security_practice.service;

import com.example.spring_security_practice.domain.dto.ResourcesDto;
import com.example.spring_security_practice.domain.entity.Resources;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

public interface ResourceService {
    void createResource(ResourcesDto dto);
    List<Resources> getResources();
    Resources getResource(Long id);
    void updateResources(Long id, ResourcesDto dto);
}
