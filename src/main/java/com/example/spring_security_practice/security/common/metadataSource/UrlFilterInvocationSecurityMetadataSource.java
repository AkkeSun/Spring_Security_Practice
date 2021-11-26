package com.example.spring_security_practice.security.common.metadataSource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * URL 방식의 자원 관리
 */
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    public UrlFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap) {
        this.requestMap = requestMap;
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
        if(requestMap != null){
            for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()){
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
        Iterator var2 = this.requestMap.entrySet().iterator();

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
}
