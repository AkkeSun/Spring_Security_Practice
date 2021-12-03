package com.example.spring_security_practice.factory;

import com.example.spring_security_practice.service.security.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import java.util.LinkedHashMap;
import java.util.List;

public class MethodResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

    private SecurityResourceService resourceService;
    private LinkedHashMap<String, List<ConfigAttribute>> resourcesMap;

    public MethodResourcesMapFactoryBean(SecurityResourceService resourceService){
        this.resourceService = resourceService;
    }


    public void init() {
       resourcesMap = resourceService.getMethodResourceList();
    }

    @Override
    public LinkedHashMap<String, List<ConfigAttribute>> getObject() {
        if (resourcesMap == null) {
            init();
        }
        return resourcesMap;
    }

    @Override
    public Class<LinkedHashMap> getObjectType() {
        return LinkedHashMap.class;
    }
    @Override
    public boolean isSingleton() {
        return true;
    }
}
