package com.example.spring_security_practice.security.common.factory;

import com.example.spring_security_practice.security.common.service.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * SecurityResourceService를 SingleTon Factory Bean 으로 생성
 */
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private SecurityResourceService securityResourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;


    public UrlResourcesMapFactoryBean(SecurityResourceService securityResourceService){
        this.securityResourceService = securityResourceService;
    }

    private void init() {
        resourceMap = securityResourceService.getResourceList();
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
