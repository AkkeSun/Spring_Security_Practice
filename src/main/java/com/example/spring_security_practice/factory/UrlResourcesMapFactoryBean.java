package com.example.spring_security_practice.factory;

import com.example.spring_security_practice.service.security.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * FactoryBean
 * 클래스의 특정 데이터를 Bean으로 생성하는 인터페이스
 */
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private SecurityResourceService resourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;

    public UrlResourcesMapFactoryBean(SecurityResourceService securityResourceService){
        this.resourceService = securityResourceService;
    }

    private void init() {
        resourceMap = resourceService.getResourceList();
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if(resourceMap == null)
            init();
        return resourceMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
