package com.example.spring_security_practice.security.common.service;

import com.example.spring_security_practice.domain.entity.AccessIp;
import com.example.spring_security_practice.domain.entity.Resources;
import com.example.spring_security_practice.repository.AccessIpRepository;
import com.example.spring_security_practice.repository.ResourcesRepository;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리소스 관린 DB 데이터를 처리하는 서비스
 */
public class SecurityResourceService {

    private ResourcesRepository resourcesRepository;
    private AccessIpRepository accessIpRepository;

    public SecurityResourceService (ResourcesRepository resourcesRepository){
        this.resourcesRepository=resourcesRepository;
    }

    //  DB에서 자원정브롤 가져와 LinkedHashMap으로 저장
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

    // 허용 가능한 IP List 가져오기
    public List<String> getAccessIpList(){
        List<String> ipList = accessIpRepository.findAll().stream().map(ip -> ip.getIpAddress()).collect(Collectors.toList());
        return ipList;
    }
}
