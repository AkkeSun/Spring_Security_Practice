package com.example.spring_security_practice.metadataSource;

import com.example.spring_security_practice.factory.UrlResourcesMapFactoryBean;
import com.example.spring_security_practice.service.security.SecurityResourceService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * URL 방식의 자원 관리
 */
public class UrlMetadataSource implements FilterInvocationSecurityMetadataSource {

    private SecurityResourceService securityResourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;

    public UrlMetadataSource(SecurityResourceService service) throws Exception {
        this.securityResourceService = service;
        this.resourceMap = new UrlResourcesMapFactoryBean(service).getObject();
    }

    /**
     * 자원에 따른 권한을 반환하는 로직 구현
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        // 현재 사용자가 요청하는 자원 객채
        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        // test
        // requestMap.put(new AntPathRequestMatcher("/myPage"), Arrays.asList(new SecurityConfig("ROLE_USER")));

        // 사전에 저장된 security 자원 중 일치하는 자원(Key)이 있다면 ROLE(value)을 리턴
        if(resourceMap != null){
            for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : resourceMap.entrySet()){
                RequestMatcher mather = entry.getKey();
                if(mather.matches(request)) return entry.getValue();

            }
        }
        return null;
    }

    /**
     * 사용하지 않는 Method
     * DefaultFilterInvocationSecurityMetadataSource를 그대로 가져왔다
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet();
        Iterator var2 = this.resourceMap.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry = (Map.Entry)var2.next();
            allAttributes.addAll((Collection)entry.getValue());
        }
        return allAttributes;
    }

    /**
     * 타입 검사
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }


    /**
     * 자원이 새로 생성될 때 실시간 데이터 업데이트
     */
    public void reload(){
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadMap = securityResourceService.getResourceList();
        Iterator<Map.Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadMap.entrySet().iterator();
        
        // 이전 인가정보 지우기
        resourceMap.clear();

        // 인가정보 다시 넣기
        while(iterator.hasNext()){
            Map.Entry<RequestMatcher, List<ConfigAttribute>> entry = iterator.next();
            resourceMap.put(entry.getKey(), entry.getValue());
        }
    }

}
